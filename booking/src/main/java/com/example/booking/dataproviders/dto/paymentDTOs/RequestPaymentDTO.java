package com.example.booking.dataproviders.dto.paymentDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPaymentDTO {

    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "AL\\d{2} [0-9A-Z]{4} [0-9A-Z]{4} [0-9A-Z]{4} [0-9A-Z]{4} [0-9A-Z]{4} [0-9A-Z]{4}", message = "Card number must be a valid Albanian IBAN")
    @Schema(description = "Card number used for the payment", example = "AL47 2121 1009 0000 0002 3569 8741")
    private String cardNumber;

    @NotBlank(message = "Card holder name is required")
//    @Schema(description = "Name of the card holder", example = "John Doe")
    private String cardHolderName;

//    @Min(value = 2024, message = "Expiration year must be greater than or equal to 2024")
////    @Schema(description = "Expiration year of the card", example = "2024")
//    private int expirationYear;
//
//    @Min(value = 1, message = "Expiration month must be between 1 and 12")
//    @Max(value = 12, message = "Expiration month must be between 1 and 12")
////    @Schema(description = "Expiration month of the card", example = "12")
//    private int expirationMonth;

    @NotBlank(message = "Expiration date is required")
    @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "Expiration date must be in the format MM/YY")
    @Schema(description = "Expiration date of the card in format MM/YY", example = "12/24")
    private String expirationDate;

    @PositiveOrZero(message = "CVV must be a positive number or zero")
//    @Pattern(regexp = "\\d{3}", message = "CVV must be exactly 3 digits")
//    @Schema(description = "CVV number of the card", example = "123")
    private int cvv;

    private Double totalPrice;
}
