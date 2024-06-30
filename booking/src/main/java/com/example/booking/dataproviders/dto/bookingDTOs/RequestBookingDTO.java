package com.example.booking.dataproviders.dto.bookingDTOs;

import com.example.booking.dataproviders.dto.paymentDTOs.RequestPaymentDTO;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RequestBookingDTO extends RequestPaymentDTO {


    private String bookingDate;

    private String checkInDate;

    private String checkOutDate;

    private Integer noOfAdults;

    private Integer noOfChildren;

    //
    private String fullName;

    private String email;

    private String address;

    private String phoneNumber;
    //

    private Long roomId;

    private String isForAnother;


}
