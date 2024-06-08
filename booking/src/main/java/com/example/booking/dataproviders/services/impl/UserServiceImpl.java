package com.example.booking.dataproviders.services.impl;

import com.example.booking.dataproviders.dto.userDTOs.RequestAdminDTO;
import com.example.booking.dataproviders.dto.userDTOs.RequestUserDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseAdminDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseUserDTO;
import com.example.booking.dataproviders.entities.Role;
import com.example.booking.dataproviders.entities.User;
import com.example.booking.dataproviders.entities.UserInfo;
import com.example.booking.dataproviders.mappers.AdminMapper;
import com.example.booking.dataproviders.mappers.UserInfoMapper;
import com.example.booking.dataproviders.mappers.UserMapper;
import com.example.booking.dataproviders.repositories.RoleRepository;
import com.example.booking.dataproviders.repositories.UserInfoRepository;
import com.example.booking.dataproviders.repositories.UserRepository;
import com.example.booking.dataproviders.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final RoleRepository roleRepository;
    private UserInfoMapper userInfoMapper;
    private UserMapper userMapper;
    private AdminMapper adminMapper;

    @Override
    public List<ResponseUserDTO> findAllUsers() {

        List<User> users = userRepository.findAll();

        List<ResponseUserDTO> responseUserDTOS = new ArrayList<>();

        for (User user : users) {

            responseUserDTOS.add(userMapper.mapToDto(user));
        }

        return responseUserDTOS;
    }

    @Override
    public ResponseUserDTO findUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.mapToDto(user);
    }

    @Override
    public ResponseUserDTO saveUser(RequestUserDTO requestUserDTO) {

        User user = userMapper.mapToEntity(requestUserDTO);

        Role role = roleRepository.findByRoleName("USER").orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);

        UserInfo userInfo = userInfoMapper.mapToEntity(requestUserDTO);
        userInfo.setUser(user);
        user.setUserInfo(userInfo);

        userRepository.save(user);

        return userMapper.mapToDto(user);
    }

    @Override
    public ResponseAdminDTO saveAdmin(RequestAdminDTO requestAdminDTO) {

        User admin = adminMapper.mapToEntity(requestAdminDTO);

        Role role = roleRepository.findByRoleName("ADMIN").orElseThrow(() -> new RuntimeException("Role not found"));
        admin.setRole(role);

        userRepository.save(admin);

        return adminMapper.mapToDto(admin);
    }


    @Override
    public ResponseUserDTO updateUser(RequestUserDTO user, Long id) {

        User foundUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        foundUser.setUsername(user.getUsername());
        foundUser.setEmail(user.getEmail());
        foundUser.setPassword(user.getPassword());
        foundUser.setPhoneNumber(user.getPhoneNumber());

        UserInfo userInfo = foundUser.getUserInfo();
        userInfo.setAddress(user.getAddress());

        foundUser.setUserInfo(userInfo);

        User updatedUser = userRepository.save(foundUser);

        return userMapper.mapToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }
}
