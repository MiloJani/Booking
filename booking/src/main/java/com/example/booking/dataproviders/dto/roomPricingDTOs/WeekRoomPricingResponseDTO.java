package com.example.booking.dataproviders.dto.roomPricingDTOs;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeekRoomPricingResponseDTO {

    private List<ResponseRoomsPricingDTO> roomPricings;

    private double tax;

    private double discount;

    private String description;

    private boolean freeParking;

    private boolean freeWifi;

    private boolean insidePool;

    private boolean freeBreakfast;
}
