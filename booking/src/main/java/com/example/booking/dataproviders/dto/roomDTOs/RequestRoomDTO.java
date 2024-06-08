package com.example.booking.dataproviders.dto.roomDTOs;

import com.example.booking.dataproviders.entities.Businesses;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestRoomDTO {

    private String roomName;

    private String capacity;

    private Double price;

    private String description;

    private String roomType;

    private String image;

    private Long businessId;

}
