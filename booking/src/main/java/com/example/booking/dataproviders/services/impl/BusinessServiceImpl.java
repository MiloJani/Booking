package com.example.booking.dataproviders.services.impl;

import com.example.booking.core.exceptions.AuthenticationFailedException;
import com.example.booking.core.exceptions.FileCouldNotBeSavedException;
import com.example.booking.core.exceptions.RecordAlreadyExistsException;
import com.example.booking.core.exceptions.RecordNotFoundException;
import com.example.booking.dataproviders.dto.businessDTOs.RequestBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessSearchDTO;
import com.example.booking.dataproviders.dto.searchDTOs.RequestSearchDTO;
import com.example.booking.dataproviders.dto.searchDTOs.ResponseSearchDTO;
import com.example.booking.dataproviders.entities.Booking;
import com.example.booking.dataproviders.entities.Businesses;
import com.example.booking.dataproviders.entities.User;
import com.example.booking.dataproviders.mappers.BusinessMapper;
import com.example.booking.dataproviders.repositories.BusinessRepository;
import com.example.booking.dataproviders.repositories.RoomRepository;
import com.example.booking.dataproviders.repositories.UserRepository;
import com.example.booking.dataproviders.services.BusinessService;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

//        return businessesPage.map(business -> {
//            int availableRooms = roomRepository.countAvailableRooms(
//                    business.getBusinessId(),
//                    searchRequest.getCheckInDate(),
//                    searchRequest.getCheckOutDate(),
//                    calculateCapacity(searchRequest.getNoOfAdults(), searchRequest.getNoOfChildren())
//            );
//
//            return businessMapper.mapToSearchDto(business, availableRooms);
//        });

        return businessesPage.map(business -> {
            int capacity = calculateCapacity(searchRequest.getNoOfAdults(), searchRequest.getNoOfChildren());
            List<Long> availableRoomIds = roomRepository.findAvailableRoomIds(
                    business.getBusinessId(),
                    searchRequest.getCheckInDate(),
                    searchRequest.getCheckOutDate(),
                    capacity
            );
            int availableRooms = availableRoomIds.size();
            ResponseSearchDTO responseSearchDTO = businessMapper.mapToSearchDto(business, availableRooms);
            responseSearchDTO.setAvailableRoomIds(Set.copyOf(availableRoomIds));
            return responseSearchDTO;
        });


    }

    private Integer calculateCapacity(Integer noOfAdults, Integer noOfChildren) {
        return (noOfAdults != null ? noOfAdults : 0) + (noOfChildren != null ? noOfChildren : 0);
    }



    //    //Search begin
//    public Page<ResponseSearchDTO> search(RequestSearchDTO searchRequest) {
//        int page = 0;
//        int size = 20;
//        Pageable pageable = PageRequest.of(page, size);
//
//        Specification<Businesses> spec = (root, query, criteriaBuilder) -> {
//            var roomJoin = root.join("rooms", JoinType.LEFT);
//
//            List<Predicate> predicates = new ArrayList<>();
//
//            // Check for non-overlapping bookings
//            if (searchRequest.getCheckInDate() != null && searchRequest.getCheckOutDate() != null) {
//                LocalDate checkInDate = searchRequest.getCheckInDate();
//                LocalDate checkOutDate = searchRequest.getCheckOutDate();
//
//                var subquery = query.subquery(Long.class);
//                var bookingRoot = subquery.from(Booking.class);
//                subquery.select(bookingRoot.get("room").get("roomId"))
//                        .where(
//                                criteriaBuilder.and(
//                                        criteriaBuilder.equal(bookingRoot.get("room").get("businesses").get("businessId"), root.get("businessId")),
//                                        criteriaBuilder.or(
//                                                // Allow rooms where the requested period is before the booking period
//                                                criteriaBuilder.lessThan(criteriaBuilder.literal(checkOutDate), bookingRoot.get("checkInDate")),
//                                                // Allow rooms where the requested period is after the booking period
//                                                criteriaBuilder.greaterThan(criteriaBuilder.literal(checkInDate), bookingRoot.get("checkOutDate")),
//                                                criteriaBuilder.greaterThan(criteriaBuilder.literal(checkOutDate), bookingRoot.get("checkOutDate"))
//                                        )
//                                )
//                        );
//                var subqueryPredicate = criteriaBuilder.not(criteriaBuilder.in(roomJoin.get("roomId")).value(subquery));
//                predicates.add(subqueryPredicate);
//            }
//
//            // Check room capacity
//            if (searchRequest.getNoOfAdults() != null || searchRequest.getNoOfChildren() != null) {
//                int totalGuests = (searchRequest.getNoOfAdults() != null ? searchRequest.getNoOfAdults() : 0) +
//                        (searchRequest.getNoOfChildren() != null ? searchRequest.getNoOfChildren() : 0);
//                if (totalGuests > 0) {
//                    predicates.add(criteriaBuilder.ge(roomJoin.get("capacity"), totalGuests));
//                }
//            }
//
//            query.distinct(true);
//            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//        };
//
//        Page<Businesses> businessesPage = businessRepository.findAll(spec, pageable);
//
//        return businessesPage.map(business -> {
//            // Calculate available rooms based on capacity
//            int totalGuests = (searchRequest.getNoOfAdults() != null ? searchRequest.getNoOfAdults() : 0) +
//                    (searchRequest.getNoOfChildren() != null ? searchRequest.getNoOfChildren() : 0);
//
//            int availableRooms = (int) business.getRooms().stream()
//                    .filter(room -> room.getCapacity() >= totalGuests)
//                    .count();
//
//            return businessMapper.mapToSearchDto(business, availableRooms);
//        });
//    }
//
//
//

//    //Search end

    @Override
    @Transactional
    public /*ResponseBusinessDTO*/String saveBusiness(RequestBusinessDTO requestBusinessDTO, String username) {


        Optional<Businesses> doesBusinessExist = businessRepository.findByBusinessName(requestBusinessDTO.getName());

        if (doesBusinessExist.isPresent()) {
            throw new RecordAlreadyExistsException("Business with the same name already exists");
        }
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
