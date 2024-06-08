package com.example.booking.endpoints;

import com.example.booking.dataproviders.dto.userDTOs.ResponseUserDTO;
import com.example.booking.dataproviders.dto.userInfoDTOs.ResponseUserInfoDTO;
import com.example.booking.dataproviders.services.UserInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/userInfo")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @GetMapping("/findById/{id}")
    public ResponseEntity<ResponseUserInfoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userInfoService.findUserInfoById(id));
    }


}
