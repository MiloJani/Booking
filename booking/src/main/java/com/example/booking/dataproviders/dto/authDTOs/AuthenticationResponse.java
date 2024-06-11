package com.example.booking.dataproviders.dto.authDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AuthenticationResponse {

    private String token;
    private String roleName;
}
