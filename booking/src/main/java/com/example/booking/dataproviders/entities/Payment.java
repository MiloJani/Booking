package com.example.booking.dataproviders.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PAYMENT")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long paymentId;

//    @Column(name = "PAYMENT_TYPE")
//    @NotBlank(message = "Payment type is required")
//    private String paymentType;

    @Column(name = "CARD_NUMBER")
    @NotBlank(message = "Card number is required")
    private String cardNumber;

    @Column(name = "CARD_HOLDER_NAME")
    @NotBlank(message = "Card holder name is required")
    private String cardHolderName;

    @Column(name = "EXPIRATION_YEAR")
    @Min(value = 2024, message = "Expiration year must be greater than or equal to 2022")
    private Integer expirationYear;

    @Column(name = "EXPIRATION_MONTH")
    @Min(value = 1, message = "Expiration month must be between 1 and 12")
    @Max(value = 12, message = "Expiration month must be between 1 and 12")
    private Integer expirationMonth;

    @Column(name = "CVV")
    @PositiveOrZero(message = "CVV must be a positive number or zero")
    private Integer cvv;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookingId")
    private Booking booking;

}
