package com.example.booking.dataproviders.services.impl;

import com.example.booking.dataproviders.dto.roomPricingDTOs.RequestRoomPricingDTO;
import com.example.booking.dataproviders.dto.roomPricingDTOs.ResponseRoomPricingDTO;
import com.example.booking.dataproviders.services.RoomPricingService;

import java.util.List;

public class RoomPricingServiceImpl implements RoomPricingService {
    @Override
    public List<ResponseRoomPricingDTO> findAllRoomPricings() {
        return List.of();
    }

    @Override
    public ResponseRoomPricingDTO findRoomPricingById(Long id) {
        return null;
    }

    @Override
    public ResponseRoomPricingDTO saveRoomPricing(RequestRoomPricingDTO requestRoomPricingDTO) {
        return null;
    }

    @Override
    public ResponseRoomPricingDTO updateRoomPricing(RequestRoomPricingDTO requestRoomPricingDTO, Long id) {
        return null;
    }

    @Override
    public void deleteRoomPricing(Long id) {

    }
}
