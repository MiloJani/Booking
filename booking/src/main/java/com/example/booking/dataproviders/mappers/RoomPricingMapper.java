package com.example.booking.dataproviders.mappers;

import com.example.booking.dataproviders.dto.roomPricingDTOs.RequestRoomPricingDTO;
import com.example.booking.dataproviders.dto.roomPricingDTOs.ResponseRoomPricingDTO;
import com.example.booking.dataproviders.entities.RoomPricing;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoomPricingMapper {

    public ResponseRoomPricingDTO mapToDto(RoomPricing roomPricing) {

        ResponseRoomPricingDTO responseRoomPricingDTO = new ResponseRoomPricingDTO();
        responseRoomPricingDTO.setRoomPricingId(roomPricing.getId());
        responseRoomPricingDTO.setDayOfWeek(roomPricing.getDayOfWeek());
        responseRoomPricingDTO.setPrice(roomPricing.getPrice());
        responseRoomPricingDTO.setRoomId(roomPricing.getRoom().getRoomId());

        return responseRoomPricingDTO;
    }

    public RoomPricing mapToEntity(RequestRoomPricingDTO requestRoomPricingDTO) {

        RoomPricing roomPricing = new RoomPricing();
        roomPricing.setDayOfWeek(requestRoomPricingDTO.getDayOfWeek());
        roomPricing.setPrice(requestRoomPricingDTO.getPrice());

        return roomPricing;
    }
}
