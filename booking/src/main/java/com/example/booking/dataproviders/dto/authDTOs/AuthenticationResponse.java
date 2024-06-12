package com.example.booking.dataproviders.dto.authDTOs;

import lombok.*;

@RequiredArgsConstructor
@Setter
@Getter
public class AuthenticationResponse {

    private String token;
    private String fullName;
    private String roleName;
    private int points;
    private int books;
}
