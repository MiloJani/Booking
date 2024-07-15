package com.example.booking.dataproviders.services.impl;

import com.example.booking.dataproviders.dto.paymentDTOs.RequestPaymentDTO;
import com.example.booking.dataproviders.dto.paymentDTOs.ResponsePaymentDTO;
import com.example.booking.dataproviders.services.PaymentService;

import java.util.List;
//safe to delete... probably will not risk it
public class PaymentServiceImpl implements PaymentService {
    @Override
    public List<ResponsePaymentDTO> findAllPayments() {
        return List.of();
    }

    @Override
    public ResponsePaymentDTO findPaymentById(Long id) {
        return null;
    }

    @Override
    public ResponsePaymentDTO savePayment(RequestPaymentDTO requestPaymentDTO) {
        return null;
    }

    @Override
    public ResponsePaymentDTO updatePayment(RequestPaymentDTO requestPaymentDTO, Long id) {
        return null;
    }

    @Override
    public void deletePayment(Long id) {

    }
}
