package com.example.booking.dataproviders.dto.userInfoDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class RequestUserInfoDTO {

    private String address;

    private LocalDate registrationDate;
}
