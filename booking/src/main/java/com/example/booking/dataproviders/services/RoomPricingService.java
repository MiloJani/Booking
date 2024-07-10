package com.example.booking.dataproviders.services;

import com.example.booking.dataproviders.dto.roomPricingDTOs.*;

import java.util.List;

public interface RoomPricingService {

    List<ResponseRoomPricingDTO> findAllRoomPricings();

    WeekRoomPricingResponseDTO getWeekRoomPricings(RequestRoomPricingsDTO dto, String username);

    ResponseRoomPricingDTO findRoomPricingById(Long id);

    ResponseRoomPricingDTO saveRoomPricing(RequestRoomPricingDTO requestRoomPricingDTO);

    ResponseRoomPricingDTO updateRoomPricing(RequestRoomPricingDTO requestRoomPricingDTO,Long id);

    void deleteRoomPricing(Long id);
}
