package com.example.booking.dataproviders.dto.bookingDTOs;

import com.example.booking.dataproviders.dto.paymentDTOs.RequestPaymentDTO;
import com.example.booking.dataproviders.dto.paymentDTOs.ResponsePaymentDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ResponseBookingDTO {

    private Long bookingId;

    private LocalDate bookingDate;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer noOfAdults;

    private Integer noOfChildren;

    private String status;

    private Long userId;

    private Long roomId;

    private Long paymentId;
}
