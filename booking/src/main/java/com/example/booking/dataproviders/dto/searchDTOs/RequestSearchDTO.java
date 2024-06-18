package com.example.booking.dataproviders.dto.searchDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RequestSearchDTO {

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer noOfAdults;
    private Integer noOfChildren;
    private int page;
    private int size;

}
