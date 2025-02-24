package com.example.booking.dataproviders.mappers;

import com.example.booking.dataproviders.dto.userDTOs.RequestUserDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseUserDTO;
import com.example.booking.dataproviders.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserMapper {

    private UserInfoMapper userInfoMapper;
    private PasswordEncoder passwordEncoder;
    public ResponseUserDTO mapToDto(User user) {

        ResponseUserDTO dto = new ResponseUserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRoleId(user.getRole().getId());
        dto.setResponseUserInfoDTO(userInfoMapper.mapToDto(user.getUserInfo()));
        return dto;
    }

    public User mapToEntity(RequestUserDTO requestUserDTO) {

        User user = new User();
        user.setUsername(requestUserDTO.getUsername());
        user.setEmail(requestUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestUserDTO.getPassword()));
        user.setPhoneNumber(requestUserDTO.getPhoneNumber());

        return user;
    }
}
