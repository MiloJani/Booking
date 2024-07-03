package com.example.booking.dataproviders.dto.roomDTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RequestAvailableRoomsDTO {

    @NotNull(message = "Business Id is mandatory")
    private Long businessId;

//    @NotNull
//    private Set<Long> roomIds;

    @NotBlank(message = "Check In Date is mandatory")
    private String checkInDate;

    @NotBlank(message = "Check Out Date is mandatory")
    private String checkOutDate;

    @NotNull(message = "Capacity is mandatory")
    private Integer capacity;

    @NotNull(message = "Page number is mandatory")
    @Min(value = 0, message = "Page number should not be negative")
    private int page;

    private String sortBy = "price"; // Default sort by price

    private String sortDirection = "asc";
}
