package com.example.booking.dataproviders.dto.authDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class AuthenticationRequest {

    private String emailOrPhoneNumber;
    String password;
}

