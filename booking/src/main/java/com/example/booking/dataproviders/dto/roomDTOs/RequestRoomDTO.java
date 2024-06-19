package com.example.booking.dataproviders.dto.roomDTOs;

import com.example.booking.dataproviders.entities.Businesses;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class RequestRoomDTO {

    @NotBlank(message = "Room name is mandatory")
    @Schema(example = "Luxury Suite")
    private String roomName;

    @NotNull(message = "Capacity cannot be null")
//    @Min(value = 1, message = "Capacity must be at least 1")
    private String capacity;

    @NotNull(message = "Price cannot be null")
//    @DecimalMin(value = "0.01", message = "Price must be greater than or equal to 0.01")
    private String price;

    @Size(max = 255, message = "Description must be less than or equal to 255 characters")
    private String description;

    @NotBlank(message = "Room type is mandatory")
    private String roomType;

    @NotNull(message = "Image file cannot be null")
    private MultipartFile image;

    @NotBlank(message = "Business name is mandatory")
    private String businessName;

}
