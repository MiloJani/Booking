package com.example.booking.dataproviders.dto.roomPricingDTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRoomsPricingDTO {
    private String date;
    private Double price;
}

