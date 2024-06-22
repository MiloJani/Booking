package com.example.booking.dataproviders.dto.businessDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class RequestBusinessDTO {

    @NotBlank(message = "Business name is mandatory")
    @Schema(example = "Milo's Amazing Hotel")
    private String name;

    @NotBlank(message = "FreeParking field is mandatory")
    private String freeParking;

    @NotBlank(message = "FreeWifi field is mandatory")
    private String freeWifi;

    @NotBlank(message = "InsidePool field is mandatory")
    private String insidePool;

    @NotBlank(message = "FreeBreakfast field is mandatory")
    private String  freeBreakfast;

    @NotNull(message = "Image file cannot be null")
    private MultipartFile image;

//    private Double tax=0.07;

//    private Long adminId;
}
