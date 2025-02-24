package com.example.booking.dataproviders.dto.userInfoDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ResponseUserInfoDTO {

    private Long userId;

    private String address;

    private String fullName;

    private LocalDate registrationDate;
}
