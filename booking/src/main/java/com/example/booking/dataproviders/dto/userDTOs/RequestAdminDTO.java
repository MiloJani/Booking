package com.example.booking.dataproviders.dto.userDTOs;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestAdminDTO {

    private String username;
    private String email;
    private String password;
    private String phoneNumber;
}
