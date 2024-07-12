package com.example.booking.dataproviders.services;

import com.example.booking.dataproviders.dto.bookingDTOs.RequestBookingDTO;
import com.example.booking.dataproviders.dto.businessDTOs.RequestBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.dto.searchDTOs.RequestSearchDTO;
import com.example.booking.dataproviders.dto.searchDTOs.ResponseSearchDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BusinessService {

        List<ResponseBusinessDTO> findAllBusinesses();

        List<String> findAllBusinessesOfAdmin(String username);

        ResponseBusinessDTO findBusinessById(Long id);

        Page<ResponseSearchDTO> search(RequestSearchDTO searchRequest,String username);

        /*ResponseBusinessDTO*/String saveBusiness(RequestBusinessDTO requestBusinessDTO,String email);

        ResponseBusinessDTO updateBusiness(RequestBusinessDTO requestBusinessDTO,Long id);

        void deleteBusiness(Long id);
}
