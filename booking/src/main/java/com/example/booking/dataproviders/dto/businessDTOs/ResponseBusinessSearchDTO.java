package com.example.booking.dataproviders.dto.businessDTOs;

import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

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
