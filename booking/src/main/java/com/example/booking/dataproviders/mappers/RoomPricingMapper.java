package com.example.booking.dataproviders.mappers;

import com.example.booking.constants.Constants;
import com.example.booking.dataproviders.dto.roomPricingDTOs.RequestRoomPricingDTO;
import com.example.booking.dataproviders.dto.roomPricingDTOs.ResponseRoomPricingDTO;
import com.example.booking.dataproviders.dto.roomPricingDTOs.ResponseRoomsPricingDTO;
import com.example.booking.dataproviders.dto.roomPricingDTOs.WeekRoomPricingResponseDTO;
import com.example.booking.dataproviders.entities.RoomPricing;
import com.example.booking.dataproviders.entities.Rooms;
import com.example.booking.dataproviders.services.utilities.ValidationUtilities;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public WeekRoomPricingResponseDTO mapToWeekRoomPricingResponseDTO(Rooms room, List<ResponseRoomsPricingDTO> responsePricings,double discount){

//        String roomImageUrl = getRoomImageUrl(room.getImage());
        String roomImageUrl = ValidationUtilities.getRoomImageUrl(room.getImage(), Constants.ROOM);

        return new WeekRoomPricingResponseDTO(
                responsePricings, room.getBusinesses().getTax(), discount,
                room.getDescription(), room.getBusinesses().isFreeParking(),
                room.getBusinesses().isFreeWifi(), room.getBusinesses().isInsidePool(),
                room.getBusinesses().isFreeBreakfast(), roomImageUrl
        );


    }


}
