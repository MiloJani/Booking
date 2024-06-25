package com.example.booking.dataproviders.mappers;

import com.example.booking.config.NetworkUtils;
import com.example.booking.dataproviders.dto.roomDTOs.RequestRoomDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import com.example.booking.dataproviders.entities.Rooms;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

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

    public static String getPublicIpAddress() {
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
            return datagramSocket.getLocalAddress().getHostAddress();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception as needed
            return "Failed to retrieve public IP";
        }
    }

    private String getRoomImageUrl(String imageFileName) {

//        String serverIp = "192.168.1.32";
        String serverIp = getPublicIpAddress();


        if (imageFileName != null) {
            String fileUrl = "http://" + serverIp + ":8080/SavedPhotos/Rooms/" + imageFileName;
            return fileUrl;
        }else {
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
