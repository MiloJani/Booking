package com.example.booking.dataproviders.services;

import com.example.booking.dataproviders.dto.userInfoDTOs.RequestUserInfoDTO;
import com.example.booking.dataproviders.dto.userInfoDTOs.ResponseUserInfoDTO;

import java.util.List;

public interface UserInfoService {

    List<ResponseUserInfoDTO> findAllUserInfo();

    ResponseUserInfoDTO findUserInfoById(Long id);

    ResponseUserInfoDTO saveUserInfo(RequestUserInfoDTO userInfoDTO);

    ResponseUserInfoDTO updateUserInfo(RequestUserInfoDTO userInfoDTO,Long id);

    void deleteUserInfo(Long id);
}
