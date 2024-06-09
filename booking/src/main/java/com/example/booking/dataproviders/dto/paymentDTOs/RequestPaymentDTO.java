package com.example.booking.dataproviders.dto.paymentDTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPaymentDTO {

//    @NotBlank(message = "Card number is required")
//    @Schema(description = "Card number used for the payment", example = "1234-5678-9012-3456")
    private String cardNumber;

//    @NotBlank(message = "Card holder name is required")
//    @Schema(description = "Name of the card holder", example = "John Doe")
    private String cardHolderName;

//    @Min(value = 2024, message = "Expiration year must be greater than or equal to 2024")
//    @Schema(description = "Expiration year of the card", example = "2024")
    private int expirationYear;

//    @Min(value = 1, message = "Expiration month must be between 1 and 12")
//    @Max(value = 12, message = "Expiration month must be between 1 and 12")
//    @Schema(description = "Expiration month of the card", example = "12")
    private int expirationMonth;

//    @PositiveOrZero(message = "CVV must be a positive number or zero")
//    @Schema(description = "CVV number of the card", example = "123")
    private int cvv;
}
