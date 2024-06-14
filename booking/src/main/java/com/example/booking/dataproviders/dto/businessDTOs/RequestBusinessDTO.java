package com.example.booking.dataproviders.dto.businessDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestBusinessDTO {

    private String businessName;

    private boolean freeParking;

    private boolean freeWifi;

    private boolean insidePool;

    private boolean freeBreakfast;

    private String image; //Multipart

    private Double tax=0.07;

    private Long adminId;
}
