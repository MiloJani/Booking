package com.example.booking.dataproviders.dto.userDTOs;

import com.example.booking.dataproviders.dto.userInfoDTOs.ResponseUserInfoDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseUserDTO {

    private Long userId;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private Long roleId;
    private ResponseUserInfoDTO responseUserInfoDTO;

}
