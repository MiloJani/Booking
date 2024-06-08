package com.example.booking.dataproviders.services;

import com.example.booking.dataproviders.dto.bookingDTOs.RequestBookingDTO;
import com.example.booking.dataproviders.dto.businessDTOs.RequestBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;

import java.util.List;

public interface BusinessService {

        List<ResponseBusinessDTO> findAllBusinesses();

        ResponseBusinessDTO findBusinessById(Long id);

        ResponseBusinessDTO saveBusiness(RequestBusinessDTO requestBusinessDTO);

        ResponseBusinessDTO updateBusiness(RequestBusinessDTO requestBusinessDTO,Long id);

        void deleteBusiness(Long id);
}
