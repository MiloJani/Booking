package com.example.booking.dataproviders.services;

import com.example.booking.dataproviders.dto.roomPricingDTOs.RequestRoomPricingDTO;
import com.example.booking.dataproviders.dto.roomPricingDTOs.ResponseRoomPricingDTO;
import com.example.booking.dataproviders.dto.roomPricingDTOs.ResponseRoomsPricingDTO;

import java.util.List;

public interface RoomPricingService {

    List<ResponseRoomPricingDTO> findAllRoomPricings();

    List<ResponseRoomsPricingDTO> getWeekRoomPricings(Long roomId);

    ResponseRoomPricingDTO findRoomPricingById(Long id);

    ResponseRoomPricingDTO saveRoomPricing(RequestRoomPricingDTO requestRoomPricingDTO);

    ResponseRoomPricingDTO updateRoomPricing(RequestRoomPricingDTO requestRoomPricingDTO,Long id);

    void deleteRoomPricing(Long id);
}
