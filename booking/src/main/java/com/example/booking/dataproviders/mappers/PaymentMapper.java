package com.example.booking.dataproviders.mappers;

import com.example.booking.dataproviders.dto.paymentDTOs.RequestPaymentDTO;
import com.example.booking.dataproviders.dto.paymentDTOs.ResponsePaymentDTO;
import com.example.booking.dataproviders.entities.Payment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PaymentMapper {

    public static Payment mapToEntity(RequestPaymentDTO requestPaymentDTO) {

        Payment payment = new Payment();
        payment.setCardNumber(requestPaymentDTO.getCardNumber());
        payment.setCardHolderName(requestPaymentDTO.getCardHolderName());
        payment.setExpirationYear(requestPaymentDTO.getExpirationYear());
        payment.setExpirationMonth(requestPaymentDTO.getExpirationMonth());
        payment.setCvv(requestPaymentDTO.getCvv());

        return payment;
    }

    public static ResponsePaymentDTO mapToDto(Payment payment) {


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
