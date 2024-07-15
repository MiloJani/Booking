package com.example.booking.dataproviders.mappers;

import com.example.booking.core.exceptions.NotCorrectDataException;
import com.example.booking.dataproviders.dto.paymentDTOs.RequestPaymentDTO;
import com.example.booking.dataproviders.dto.paymentDTOs.ResponsePaymentDTO;
import com.example.booking.dataproviders.entities.Payment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;

@AllArgsConstructor
@Component
public class PaymentMapper {

    public  Payment mapToEntity(RequestPaymentDTO requestPaymentDTO) {

        Payment payment = new Payment();
        payment.setCardNumber(requestPaymentDTO.getCardNumber());
        payment.setCardHolderName(requestPaymentDTO.getCardHolderName());

        try {
            String[] expirationParts = requestPaymentDTO.getExpirationDate().split("/");
            if (expirationParts.length != 2) {
                throw new NotCorrectDataException("Invalid expiration date format. Please use MM/YY format.");
            }

            int expirationMonth = Integer.parseInt(expirationParts[0]);
            int expirationYear = Integer.parseInt("20" + expirationParts[1]);

            // Check if card is expired
            LocalDate currentDate = LocalDate.now();
            YearMonth cardExpiry = YearMonth.of(expirationYear, expirationMonth);
            if (cardExpiry.isBefore(YearMonth.from(currentDate))) {
                throw new NotCorrectDataException("Card is expired");
            }

            payment.setExpirationMonth(expirationMonth);
            payment.setExpirationYear(expirationYear);

            int cvv = requestPaymentDTO.getCvv();
            if (String.valueOf(cvv).length() != 3) {
                throw new NotCorrectDataException("CVV must be exactly 3 digits long.");
            }
            payment.setCvv(cvv);

        } catch (NumberFormatException | DateTimeException e) {
            throw new NotCorrectDataException("Invalid expiration date. Please ensure the date is in MM/YY format and is valid.");
        }

        return payment;
    }

    public  ResponsePaymentDTO mapToDto(Payment payment) {


        ResponsePaymentDTO responsePaymentDTO = new ResponsePaymentDTO();
        responsePaymentDTO.setPaymentId(payment.getPaymentId());
        responsePaymentDTO.setCardNumber(payment.getCardNumber());
        responsePaymentDTO.setCardHolderName(payment.getCardHolderName());
        responsePaymentDTO.setExpirationYear(payment.getExpirationYear());
        responsePaymentDTO.setExpirationMonth(payment.getExpirationMonth());
        responsePaymentDTO.setCvv(payment.getCvv());
        responsePaymentDTO.setBookingId(payment.getBooking() != null ? payment.getBooking().getBookingId() : null);

        return responsePaymentDTO;
    }
}
