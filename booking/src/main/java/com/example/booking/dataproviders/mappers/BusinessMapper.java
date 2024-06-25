package com.example.booking.dataproviders.mappers;

import com.example.booking.config.NetworkUtils;
import com.example.booking.dataproviders.dto.businessDTOs.RequestBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessSearchDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import com.example.booking.dataproviders.dto.searchDTOs.ResponseSearchDTO;
import com.example.booking.dataproviders.entities.Businesses;
import com.example.booking.dataproviders.entities.Rooms;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class BusinessMapper {

    private RoomMapper roomMapper;
    private static final String UPLOAD_DIR = "C:\\Users\\USER\\Desktop\\BookingProject\\Booking\\booking\\src\\main\\resources\\images\\businesses\\";


    public ResponseBusinessDTO mapToDto(Businesses businesses) {

        ResponseBusinessDTO responseBusinessDTO = new ResponseBusinessDTO();
        responseBusinessDTO.setBusinessId(businesses.getBusinessId());
        responseBusinessDTO.setBusinessName(businesses.getBusinessName());
        responseBusinessDTO.setFreeBreakfast(businesses.isFreeBreakfast());
        responseBusinessDTO.setFreeParking(businesses.isFreeParking());
        responseBusinessDTO.setFreeWifi(businesses.isFreeWifi());
        responseBusinessDTO.setInsidePool(businesses.isInsidePool());
        responseBusinessDTO.setTax(businesses.getTax());
        responseBusinessDTO.setImage(businesses.getImage());
        responseBusinessDTO.setAdminId(businesses.getAdmin().getUserId());

        Set<Rooms> rooms = businesses.getRooms();
        if (rooms != null) {
            Set<ResponseRoomDTO> responseRoomDTOS = new HashSet<>();
            for (Rooms room : rooms) {
                responseRoomDTOS.add(roomMapper.mapToDto(room));
            }
            responseBusinessDTO.setRooms(responseRoomDTOS);
        }else responseBusinessDTO.setRooms(Collections.emptySet());

        return responseBusinessDTO;
    }

    public Businesses mapToEntity(RequestBusinessDTO requestBusinessDTO) {

        Businesses businesses = new Businesses();
        businesses.setBusinessName(requestBusinessDTO.getName());
        businesses.setFreeBreakfast(Boolean.parseBoolean(requestBusinessDTO.getFreeBreakfast()));
        businesses.setFreeParking(Boolean.parseBoolean(requestBusinessDTO.getFreeParking()));
        businesses.setFreeWifi(Boolean.parseBoolean(requestBusinessDTO.getFreeWifi()));
        businesses.setInsidePool(Boolean.parseBoolean(requestBusinessDTO.getInsidePool()));
//        businesses.setTax(requestBusinessDTO.getTax());
//        businesses.setImage(requestBusinessDTO.getImage());

        return businesses;
    }

    public ResponseSearchDTO mapToSearchDto(Businesses businesses, int availableRooms) {
        ResponseBusinessSearchDTO responseBusinessSearchDTO = new ResponseBusinessSearchDTO();
        responseBusinessSearchDTO.setBusinessId(businesses.getBusinessId());
        responseBusinessSearchDTO.setBusinessName(businesses.getBusinessName());
        responseBusinessSearchDTO.setFreeParking(businesses.isFreeParking());
        responseBusinessSearchDTO.setFreeWifi(businesses.isFreeWifi());
        responseBusinessSearchDTO.setInsidePool(businesses.isInsidePool());
        responseBusinessSearchDTO.setFreeBreakfast(businesses.isFreeBreakfast());
        responseBusinessSearchDTO.setTax(businesses.getTax());


//        if (imageFileName != null) {
//            try {
//                Path imagePath = Paths.get(UPLOAD_DIR, imageFileName);
//                byte[] imageBytes = Files.readAllBytes(imagePath);
//                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
//                responseBusinessSearchDTO.setImage(base64Image);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        String imageFileName = businesses.getImage();
        responseBusinessSearchDTO.setImage(getBusinessImageUrl(imageFileName));

        ResponseSearchDTO responseSearchDTO = new ResponseSearchDTO();
        responseSearchDTO.setResponseBusinessSearchDTO(responseBusinessSearchDTO);
        responseSearchDTO.setFreeRooms(availableRooms);

        return responseSearchDTO;
    }

//    private String getBusinessImageUrl(String imageFileName) {
//        if (imageFileName != null) {
//            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
//                    .path("/images/businesses/")
//                    .path(imageFileName)
//                    .toUriString();
//            return fileUrl;
//        }
//        return null;
//    }

//    private String getBusinessImageUrl(String imageFileName) {
//        //            String serverIp = NetworkUtils.getServerIpAddress();
//        String serverIp = "192.168.1.32";
//        if (imageFileName != null) {
//            String fileUrl = "http://" + serverIp + ":8080/SavedPhotos/Businesses/" + imageFileName;
//            return fileUrl;
//        }else {
//            return "http://" + serverIp + ":8080/SavedPhotos/Businesses/default.png";
//        }
//    }

    public static String getPublicIpAddress() {
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
            return datagramSocket.getLocalAddress().getHostAddress();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception as needed
            return "Failed to retrieve public IP";
        }
    }

    public String getBusinessImageUrl(String imageFileName) {
        String serverIp = getPublicIpAddress(); // dynamic ip

        if (imageFileName != null) {
            return "http://" + serverIp + ":8080/SavedPhotos/Businesses/" + imageFileName;
        } else {
            return "http://" + serverIp + ":8080/SavedPhotos/Businesses/default.png";
        }
    }
}
