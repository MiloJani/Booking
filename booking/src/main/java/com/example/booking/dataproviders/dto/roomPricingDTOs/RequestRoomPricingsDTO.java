package com.example.booking.dataproviders.dto.roomPricingDTOs;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestRoomPricingsDTO {

    private Long roomId;
    private String checkInDate;
    private String checkOutDate;
}
