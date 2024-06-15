package com.example.booking.dataproviders.mappers;

import com.example.booking.dataproviders.dto.roomDTOs.RequestRoomDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import com.example.booking.dataproviders.entities.Rooms;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoomMapper {

    public ResponseRoomDTO mapToDto(Rooms room){

        ResponseRoomDTO responseRoomDTO = new ResponseRoomDTO();
        responseRoomDTO.setRoomId(room.getRoomId());
        responseRoomDTO.setRoomName(room.getRoomName());
        responseRoomDTO.setCapacity(room.getCapacity());
        responseRoomDTO.setPrice(room.getPrice());
        responseRoomDTO.setDescription(room.getDescription());
        responseRoomDTO.setImage(room.getImage());
        responseRoomDTO.setRoomType(room.getRoomType());
        responseRoomDTO.setBusinessId(room.getBusinesses().getBusinessId());
        return responseRoomDTO;
    }

    public Rooms mapToEntity(RequestRoomDTO requestRoomDTO){

        Rooms rooms = new Rooms();
        rooms.setRoomName(requestRoomDTO.getRoomName());
        rooms.setCapacity(requestRoomDTO.getCapacity());
        rooms.setPrice(requestRoomDTO.getPrice());
        rooms.setDescription(requestRoomDTO.getDescription());
//        rooms.setImage(requestRoomDTO.getImage());
        rooms.setRoomType(requestRoomDTO.getRoomType());
        return rooms;
    }
}
