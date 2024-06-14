package com.example.booking.dataproviders.dto.businessDTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class RequestBusinessDTO {

    @NotBlank()
    private String businessName;

    private boolean freeParking;

    private boolean freeWifi;

    private boolean insidePool;

    private boolean freeBreakfast;

    private MultipartFile image;

//    private Double tax=0.07;

//    private Long adminId;
}
