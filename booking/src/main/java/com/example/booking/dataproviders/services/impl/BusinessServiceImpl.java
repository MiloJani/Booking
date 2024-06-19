package com.example.booking.dataproviders.services.impl;

import com.example.booking.core.exceptions.AuthenticationFailedException;
import com.example.booking.core.exceptions.FileCouldNotBeSavedException;
import com.example.booking.core.exceptions.RecordNotFoundException;
import com.example.booking.dataproviders.dto.businessDTOs.RequestBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessSearchDTO;
import com.example.booking.dataproviders.dto.searchDTOs.RequestSearchDTO;
import com.example.booking.dataproviders.dto.searchDTOs.ResponseSearchDTO;
import com.example.booking.dataproviders.entities.Businesses;
import com.example.booking.dataproviders.entities.User;
import com.example.booking.dataproviders.mappers.BusinessMapper;
import com.example.booking.dataproviders.repositories.BusinessRepository;
import com.example.booking.dataproviders.repositories.RoomRepository;
import com.example.booking.dataproviders.repositories.UserRepository;
import com.example.booking.dataproviders.services.BusinessService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private BusinessRepository businessRepository;
    private BusinessMapper businessMapper;
    private UserRepository userRepository;
    private RoomRepository roomRepository;

    @Override
    public List<ResponseBusinessDTO> findAllBusinesses() {

        List<Businesses> businesses = businessRepository.findAll();

        List<ResponseBusinessDTO> responseBusinessDTOS = new ArrayList<>();

        for (Businesses business : businesses) {
            responseBusinessDTOS.add(businessMapper.mapToDto(business));
        }

        return responseBusinessDTOS;
    }

    @Override
    public List<String> findAllBusinessesOfAdmin(String username) {

        User admin = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        if (!admin.getRole().getRoleName().equals("ADMIN")) {
            throw new AuthenticationFailedException("User does not have sufficient privileges to add a business");
        }

        List<Businesses> businesses = businessRepository.findByAdmin(admin);

        return businesses.stream()
                .map(Businesses::getBusinessName)
                .collect(Collectors.toList());
    }


    @Override
    public ResponseBusinessDTO findBusinessById(Long id) {

        Businesses business = businessRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Business not found"));

        return businessMapper.mapToDto(business);
    }

    @Override
    public Page<ResponseSearchDTO> search(RequestSearchDTO searchRequest) {
        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);
        Page<Businesses> businessesPage = businessRepository.findAll(pageable);


        return businessesPage.map(business -> {
            int availableRooms = roomRepository.countAvailableRooms(
                    business.getBusinessId(),
                    searchRequest.getCheckInDate(),
                    searchRequest.getCheckOutDate(),
                    calculateCapacity(searchRequest.getNoOfAdults(), searchRequest.getNoOfChildren())
            );

            return businessMapper.mapToSearchDto(business, availableRooms);
        });


    }

    private Integer calculateCapacity(Integer noOfAdults, Integer noOfChildren) {
        return (noOfAdults != null ? noOfAdults : 0) + (noOfChildren != null ? noOfChildren : 0);
    }



    //    //Search begin
//@Override
//public Page<ResponseSearchDTO> searchBookings(RequestSearchDTO searchRequest) {
//    // Add sorting by room price in ascending order. For descending order, use Sort.by("price").descending()
//    Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), Sort.by("rooms.price").ascending());
//    Page<Businesses> businessPage = businessRepository.findAll(pageable);
//
//    List<ResponseSearchDTO> bookingDTOs = businessPage.getContent().stream()
//            .map(business -> {
//                long freeRooms = business.getRooms().stream()
//                        .filter(room -> isRoomFree(room, searchRequest))
//                        .count();
//
//                ResponseBusinessDTO responseBusinessDTO = businessMapper.mapToDto(business);
//
//                ResponseSearchDTO responseSearchDTO = new ResponseSearchDTO();
//                responseSearchDTO.setResponseBusinessDTO(responseBusinessDTO);
//                responseSearchDTO.setFreeRooms((int) freeRooms);
//
//                return responseSearchDTO;
//            })
//            .sorted(Comparator.comparing(dto -> dto.getResponseBusinessDTO().getRooms().stream()
//                    .mapToDouble(ResponseRoomDTO::getPrice)
//                    .min()
//                    .orElse(Double.MAX_VALUE)))  // Sorting by minimum room price in each business
//            .collect(Collectors.toList());
//
//    return new PageImpl<>(bookingDTOs, pageable, businessPage.getTotalElements());
//}
//
//    private boolean isRoomFree(Rooms room, RequestSearchDTO searchRequest) {
//        int totalGuests = (searchRequest.getNoOfAdults() != null ? searchRequest.getNoOfAdults() : 0) +
//                (searchRequest.getNoOfChildren() != null ? searchRequest.getNoOfChildren() : 0);
//
//        if (room.getCapacity() < totalGuests) {
//            return false;
//        }
//
//        LocalDate checkInDate = searchRequest.getCheckInDate();
//        LocalDate checkOutDate = searchRequest.getCheckOutDate();
//
//        Specification<Booking> roomSpec = (root, query, criteriaBuilder) -> criteriaBuilder.and(
//                criteriaBuilder.equal(root.get("room"), room),
//                criteriaBuilder.or(
//                        criteriaBuilder.lessThan(root.get("checkOutDate"), checkInDate),
//                        criteriaBuilder.greaterThan(root.get("checkInDate"), checkOutDate)
//                )
//        );
//
//        boolean isFree = bookingRepository.findAll(roomSpec).isEmpty();
//        System.out.println("Room: " + room.getRoomName() + ", Is Free: " + isFree);
//
//        return isFree;
//    }
//    //Search end

    @Override
    @Transactional
    public /*ResponseBusinessDTO*/String saveBusiness(RequestBusinessDTO requestBusinessDTO, String username) {

        Businesses businesses = businessMapper.mapToEntity(requestBusinessDTO);

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        if (!user.getRole().getRoleName().equals("ADMIN")) {
            throw new AuthenticationFailedException("User does not have sufficient privileges to add a business");
        }

        businesses.setAdmin(user);

        MultipartFile image = requestBusinessDTO.getImage();
        if (image != null && !image.isEmpty()) {
            if (image.getSize() > 100 * 1024) {
                throw new FileCouldNotBeSavedException("File size must be less than or equal to 100KB");
            }
            try {

                String uploadDir = "C:\\Users\\USER\\Desktop\\BookingProject\\Booking\\booking\\src\\main\\resources\\images\\businesses\\";

                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

                File file = new File(uploadDir + fileName);

                image.transferTo(file);

                businesses.setImage(fileName);
            } catch (IOException e) {
                throw new FileCouldNotBeSavedException("Failed to save image file");
            }
        }

        businesses.setTax(0.07);
        Businesses savedBusiness = businessRepository.save(businesses);

//        return businessMapper.mapToDto(savedBusiness);
        return "Business saved successfully";
    }

    @Override
    public ResponseBusinessDTO updateBusiness(RequestBusinessDTO requestBusinessDTO, Long id) {

        Businesses foundBusiness = businessRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Business not found"));

        foundBusiness.setBusinessName(requestBusinessDTO.getName());
        foundBusiness.setFreeBreakfast(Boolean.parseBoolean(requestBusinessDTO.getFreeBreakfast()));
        foundBusiness.setFreeParking(Boolean.parseBoolean(requestBusinessDTO.getFreeParking()));
        foundBusiness.setFreeWifi(Boolean.parseBoolean(requestBusinessDTO.getFreeWifi()));
        foundBusiness.setInsidePool(Boolean.parseBoolean(requestBusinessDTO.getFreeBreakfast()));
//        foundBusiness.setTax(requestBusinessDTO.getTax());
//        foundBusiness.setImage(requestBusinessDTO.getImage());
        Businesses updatedBusiness = businessRepository.save(foundBusiness);
        return businessMapper.mapToDto(updatedBusiness);
    }

    @Override
    public void deleteBusiness(Long id) {

        Businesses businesses = businessRepository.findById(id).orElseThrow(() -> new RuntimeException("Business not found"));

        businessRepository.delete(businesses);
    }
}
