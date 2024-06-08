package com.example.booking.dataproviders.services;

import com.example.booking.dataproviders.dto.paymentDTOs.RequestPaymentDTO;
import com.example.booking.dataproviders.dto.paymentDTOs.ResponsePaymentDTO;

import java.util.List;

public interface PaymentService {

    List<ResponsePaymentDTO> findAllPayments();

    ResponsePaymentDTO findPaymentById(Long id);

    ResponsePaymentDTO savePayment(RequestPaymentDTO requestPaymentDTO);

    ResponsePaymentDTO updatePayment(RequestPaymentDTO requestPaymentDTO,Long id);

    void deletePayment(Long id);
}
