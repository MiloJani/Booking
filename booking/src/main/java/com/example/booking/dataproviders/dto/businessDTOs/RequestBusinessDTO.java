package com.example.booking.dataproviders.dto.businessDTOs;

import com.example.booking.dataproviders.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    private String image;

    private Double tax=0.07;

    private Long adminId;
}
