package com.example.booking.dataproviders.services.impl;

import com.example.booking.core.exceptions.AuthenticationFailedException;
import com.example.booking.core.exceptions.RecordNotFoundException;
import com.example.booking.dataproviders.dto.bookingDTOs.RequestBookingDTO;
import com.example.booking.dataproviders.dto.businessDTOs.RequestBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.entities.Businesses;
import com.example.booking.dataproviders.entities.User;
import com.example.booking.dataproviders.mappers.BusinessMapper;
import com.example.booking.dataproviders.repositories.BusinessRepository;
import com.example.booking.dataproviders.repositories.UserRepository;
import com.example.booking.dataproviders.services.BusinessService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Transactional
    public ResponseBusinessDTO saveBusiness(RequestBusinessDTO requestBusinessDTO, String username) {

        Businesses businesses = businessMapper.mapToEntity(requestBusinessDTO);

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        if (!user.getRole().getRoleName().equals("ADMIN")) {
            throw new AuthenticationFailedException("User does not have sufficient privileges to add a business");
        }

        MultipartFile image = requestBusinessDTO.getImage();
        if (image != null && !image.isEmpty()) {
            if (image.getSize() > 100 * 1024) {
                throw new RuntimeException("File size must be less than or equal to 100KB");
            }
            try {

                String uploadDir = "C:\\Users\\USER\\Desktop\\BookingProject\\Booking\\booking\\src\\main\\resources\\images\\";

                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

                File file = new File(uploadDir + fileName);

                image.transferTo(file);

                businesses.setImage(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image file", e);
            }
        }

        businesses.setTax(0.07);
        Businesses savedBusiness = businessRepository.save(businesses);

        //File file = new File("path/to/image/folder/" + fileName);
        //file.transferTo(new File(filePath))
        return businessMapper.mapToDto(savedBusiness);
    }

    @Override
    public ResponseBusinessDTO updateBusiness(RequestBusinessDTO requestBusinessDTO, Long id) {

        Businesses foundBusiness = businessRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Business not found"));

        foundBusiness.setBusinessName(requestBusinessDTO.getBusinessName());
        foundBusiness.setFreeBreakfast(requestBusinessDTO.isFreeBreakfast());
        foundBusiness.setFreeParking(requestBusinessDTO.isFreeParking());
        foundBusiness.setFreeWifi(requestBusinessDTO.isFreeWifi());
        foundBusiness.setInsidePool(requestBusinessDTO.isInsidePool());
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
