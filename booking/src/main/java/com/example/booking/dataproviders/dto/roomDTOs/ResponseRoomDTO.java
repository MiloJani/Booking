package com.example.booking.dataproviders.dto.roomDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseRoomDTO {

    private Long roomId;

    private String roomName;

    private Integer capacity;

    private Double price;

    private String description;

    private String roomType;

    private String image;

    private Long businessId;

    private Double totalPriceForNights;

    private Long numberOfNights;
}
