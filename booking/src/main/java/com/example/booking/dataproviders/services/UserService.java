package com.example.booking.dataproviders.services;

import com.example.booking.dataproviders.dto.userDTOs.RequestAdminDTO;
import com.example.booking.dataproviders.dto.userDTOs.RequestUserDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseAdminDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseUserDTO;

import java.util.List;

public interface UserService {

    List<ResponseUserDTO> findAllUsers();

    ResponseUserDTO findUserById(Long id);

    ResponseUserDTO saveUser(RequestUserDTO user);

    ResponseAdminDTO saveAdmin(RequestAdminDTO user);

    ResponseUserDTO updateUser(RequestUserDTO user,Long id);

    void deleteUser(Long id);
}
