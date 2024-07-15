package com.example.booking.dataproviders.dto.businessDTOs;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ResponseBusinessSearchDTO {

    private Long businessId;

    private String businessName;

    private boolean freeParking;

    private boolean freeWifi;

    private boolean insidePool;

    private boolean freeBreakfast;

    private String image;

    private Double tax=0.07;

}
