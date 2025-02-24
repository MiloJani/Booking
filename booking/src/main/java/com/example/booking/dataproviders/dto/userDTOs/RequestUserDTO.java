package com.example.booking.dataproviders.dto.userDTOs;

import com.example.booking.dataproviders.dto.userInfoDTOs.RequestUserInfoDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestUserDTO extends RequestUserInfoDTO {

    private String username;
    private String email;
    private String password;
    private String phoneNumber;
}
