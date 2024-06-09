package com.example.booking.dataproviders.dto.roomPricingDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;

@Getter
@Setter
public class ResponseRoomPricingDTO {

    private Long roomPricingId;

    private DayOfWeek dayOfWeek;

    private Double price;

    private Long roomId;
}
