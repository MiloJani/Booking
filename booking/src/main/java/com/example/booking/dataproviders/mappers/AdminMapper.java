package com.example.booking.dataproviders.mappers;

import com.example.booking.dataproviders.dto.userDTOs.RequestAdminDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseAdminDTO;
import com.example.booking.dataproviders.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AdminMapper {

    private final PasswordEncoder passwordEncoder;

    public ResponseAdminDTO mapToDto(User user) {

        ResponseAdminDTO dto = new ResponseAdminDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRoleId(user.getRole().getId());
        return dto;
    }

    public User mapToEntity(RequestAdminDTO requestAdminDTO) {

        User user = new User();
        user.setUsername(requestAdminDTO.getUsername());
        user.setEmail(requestAdminDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestAdminDTO.getPassword()));
        user.setPhoneNumber(requestAdminDTO.getPhoneNumber());

        return user;
    }
}
