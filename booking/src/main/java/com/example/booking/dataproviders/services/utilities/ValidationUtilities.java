package com.example.booking.dataproviders.services.utilities;

import com.example.booking.constants.Constants;
import com.example.booking.core.exceptions.FileCouldNotBeSavedException;
import com.example.booking.core.exceptions.NotCorrectDataException;
import com.example.booking.dataproviders.dto.roomDTOs.RequestRoomDTO;
import com.example.booking.dataproviders.dto.searchDTOs.RequestSearchDTO;
import com.example.booking.dataproviders.entities.Rooms;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
//@AllArgsConstructor
public class ValidationUtilities {

    public static void validateDates(String checkIn, String checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new NotCorrectDataException(Constants.CHECKED_IN_OUT_DATES_NOT_FILLED);
        }

        LocalDate checkInDate;
        LocalDate checkOutDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            checkInDate = LocalDate.parse(checkIn, formatter);
            checkOutDate = LocalDate.parse(checkOut, formatter);
        } catch (Exception e) {
            throw new NotCorrectDataException(Constants.INVALID_DATE_FORMAT);
        }

        if (checkInDate.isBefore(LocalDate.now()) || checkOutDate.isBefore(LocalDate.now()) || checkOutDate.isBefore(checkInDate)) {
            throw new NotCorrectDataException(Constants.INCORRECT_CHECKED_IN_OUT_DATES);
        }
    }


    public static String transferImage(MultipartFile image, String uploadDir) {

        if (image == null || image.isEmpty()) {
            throw new NotCorrectDataException(Constants.NO_IMAGE_PROVIDED);
        }

        if (image.getSize() > 100 * 1024) {
            throw new FileCouldNotBeSavedException(Constants.FILE_TOO_LARGE);
        }

        try {
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            File file = new File(uploadDir + fileName);
            image.transferTo(file);
            return fileName;
        } catch (IOException e) {
            throw new FileCouldNotBeSavedException(Constants.FILE_SAVE_FAILED);
        }
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

    public static String getRoomImageUrl(String imageFileName,String folder) {

//        String serverIp = "https://e51b-79-106-203-48.ngrok-free.app"; //per ngrok hiq http dhe :8080
        String serverIp = getPublicIpAddress();


        if (imageFileName != null) {
//            String fileUrl =  serverIp + "/SavedPhotos/"+folder+"/" + imageFileName; //ngrok
            String fileUrl = "http://" + serverIp + ":8080/SavedPhotos/"+folder+"/" + imageFileName;
            return fileUrl;
        } else {
//            return serverIp + "/SavedPhotos/"+folder+"/" + imageFileName; //ngrok
            return "http://" + serverIp + ":8080/SavedPhotos/"+folder+"/default.jpeg";
        }

    }

}
