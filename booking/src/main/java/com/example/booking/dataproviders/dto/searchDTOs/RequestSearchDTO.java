package com.example.booking.dataproviders.dto.searchDTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RequestSearchDTO {

    @NotBlank(message = "Check In Date is mandatory")
    private String checkInDate;

    @NotBlank(message = "Check Out Date is mandatory")
    private String checkOutDate;

    @Min(value = 0, message = "Number of adults should not be negative")
    private Integer noOfAdults;

    @Min(value = 0, message = "Number of children should not be negative")
    private Integer noOfChildren;

    @NotNull(message = "Page number is mandatory")
    @Min(value = 0, message = "Page number should not be negative")
    private int page;


}
