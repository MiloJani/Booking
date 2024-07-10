package com.example.booking.dataproviders.dto.bookingDTOs;

import com.example.booking.dataproviders.dto.paymentDTOs.RequestPaymentDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RequestBookingDTO extends RequestPaymentDTO {

    @Schema(description = "Booking date in format YYYY-MM-DD", example = "2024-07-01")
    @NotBlank(message = "Booking date is required")
    private String bookingDate;

    @Schema(description = "Check-in date in format YYYY-MM-DD", example = "2024-07-10")
    @NotBlank(message = "Check-in date is required")
    private String checkInDate;

    @Schema(description = "Check-out date in format YYYY-MM-DD", example = "2024-07-15")
    @NotBlank(message = "Check-out date is required")
    private String checkOutDate;

    private Integer noOfAdults;

    private Integer noOfChildren;

    //
    @Schema(description = "Full name of the person making the booking", example = "John Doe")
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Schema(description = "Email of the person making the booking, must be a Gmail address", example = "johndoe@gmail.com")
    @Email(regexp = ".+@gmail\\.com", message = "Email must be a Gmail address")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "Address of the person making the booking, must be an Albanian address", example = "Rruga Dervish Hima, Tirana, 1001")
    @NotBlank(message = "Address is required")
    @Pattern(regexp = ".*\\d{4}.*", message = "Address must be an Albanian address")
    private String address;

    @Schema(description = "Phone number of the person making the booking, must be a valid Albanian number", example = "+355682345678")
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\+355\\d{9}", message = "Phone number must be a valid Albanian number")
    private String phoneNumber;
    //

    @NotNull(message = "Room ID is required")
    private Long roomId;

    @NotBlank(message = "This field is required")
    private String isForAnother="true";


}
