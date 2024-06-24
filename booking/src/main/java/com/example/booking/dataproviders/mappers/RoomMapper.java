package com.example.booking.dataproviders.mappers;

import com.example.booking.config.NetworkUtils;
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

        String imageFileName = room.getImage();
        responseRoomDTO.setImage(getRoomImageUrl(imageFileName));

        responseRoomDTO.setRoomType(room.getRoomType());
        responseRoomDTO.setBusinessId(room.getBusinesses().getBusinessId());
        return responseRoomDTO;
    }

    private String getRoomImageUrl(String imageFileName) {
        if (imageFileName != null) {
            String serverIp = NetworkUtils.getServerIpAddress();
            String fileUrl = "http://" + serverIp + ":8080/SavedPhotos/Rooms/" + imageFileName;
            return fileUrl;
        }else {
            String serverIp = NetworkUtils.getServerIpAddress();
            return "http://" + serverIp + ":8080/SavedPhotos/Rooms/default.png";
        }
    }

    public Rooms mapToEntity(RequestRoomDTO requestRoomDTO){

        Rooms rooms = new Rooms();
        rooms.setRoomName(requestRoomDTO.getRoomName());
        rooms.setCapacity(Integer.parseInt(requestRoomDTO.getCapacity()));
        rooms.setPrice(Double.parseDouble(requestRoomDTO.getPrice()));
        rooms.setDescription(requestRoomDTO.getDescription());
//        rooms.setImage(requestRoomDTO.getImage());
        rooms.setRoomType(requestRoomDTO.getRoomType());
        return rooms;
    }
}
