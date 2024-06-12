package com.example.booking.dataproviders.mappers;

import com.example.booking.dataproviders.dto.userInfoDTOs.RequestUserInfoDTO;
import com.example.booking.dataproviders.dto.userInfoDTOs.ResponseUserInfoDTO;
import com.example.booking.dataproviders.entities.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserInfoMapper {

    public ResponseUserInfoDTO mapToDto(UserInfo userInfo) {

        ResponseUserInfoDTO dto = new ResponseUserInfoDTO();
        dto.setUserId(userInfo.getUserId());
        dto.setAddress(userInfo.getAddress());
        dto.setFullName(userInfo.getFullName());
        dto.setRegistrationDate(userInfo.getRegisterDate());
        return dto;
    }

    public UserInfo mapToEntity(RequestUserInfoDTO requestUserInfoDTO){

        UserInfo userInfo = new UserInfo();
        userInfo.setAddress(requestUserInfoDTO.getAddress());
        userInfo.setFullName(requestUserInfoDTO.getFullName());
        userInfo.setRegisterDate(requestUserInfoDTO.getRegistrationDate());
        return userInfo;
    }
}
