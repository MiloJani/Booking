package com.example.booking.dataproviders.dto.userDTOs;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseAdminDTO {

    private Long userId;
    private String username;
    private String email;
    private String phoneNumber;
    private Long roleId;
}
