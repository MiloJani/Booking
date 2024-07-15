package com.example.booking.dataproviders.mappers;

import com.example.booking.constants.Constants;
import com.example.booking.dataproviders.dto.businessDTOs.RequestBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessSearchDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import com.example.booking.dataproviders.dto.searchDTOs.ResponseSearchDTO;
import com.example.booking.dataproviders.entities.Businesses;
import com.example.booking.dataproviders.entities.Rooms;
import com.example.booking.dataproviders.services.utilities.ValidationUtilities;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class BusinessMapper {

    private RoomMapper roomMapper;
    private static final String UPLOAD_DIR = "C:\\Users\\USER\\Desktop\\SavedPhotos\\Businesses\\";


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
        responseBusinessSearchDTO.setImage(ValidationUtilities.getRoomImageUrl(imageFileName, Constants.BUSINESS));

        ResponseSearchDTO responseSearchDTO = new ResponseSearchDTO();
        responseSearchDTO.setResponseBusinessSearchDTO(responseBusinessSearchDTO);
        responseSearchDTO.setFreeRooms(availableRooms);

        return responseSearchDTO;
    }

}
