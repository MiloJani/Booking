package com.example.booking.dataproviders.services.impl;

import com.example.booking.dataproviders.dto.userInfoDTOs.RequestUserInfoDTO;
import com.example.booking.dataproviders.dto.userInfoDTOs.ResponseUserInfoDTO;
import com.example.booking.dataproviders.entities.UserInfo;
import com.example.booking.dataproviders.mappers.UserInfoMapper;
import com.example.booking.dataproviders.repositories.UserInfoRepository;
import com.example.booking.dataproviders.services.UserInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private UserInfoRepository userInfoRepository;
    private UserInfoMapper userInfoMapper;

    @Override
    public List<ResponseUserInfoDTO> findAllUserInfo() {
        return List.of();
    }

    @Override
    public ResponseUserInfoDTO findUserInfoById(Long id) {

        UserInfo userInfo = userInfoRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));

        return userInfoMapper.mapToDto(userInfo);
    }

    @Override
    public ResponseUserInfoDTO saveUserInfo(RequestUserInfoDTO userInfoDTO) {
        return null;
    }

    @Override
    public ResponseUserInfoDTO updateUserInfo(RequestUserInfoDTO userInfoDTO, Long id) {
        return null;
    }

    @Override
    public void deleteUserInfo(Long id) {

    }
}
