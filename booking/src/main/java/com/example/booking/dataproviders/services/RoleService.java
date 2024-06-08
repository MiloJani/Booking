package com.example.booking.dataproviders.services;

import com.example.booking.dataproviders.dto.roleDTOs.RequestRoleDTO;
import com.example.booking.dataproviders.dto.roleDTOs.ResponseRoleDTO;

import java.util.List;

public interface RoleService {

    List<ResponseRoleDTO> findAllRoles();

    ResponseRoleDTO findRoleById(Long id);

    ResponseRoleDTO saveRole(RequestRoleDTO role);

    ResponseRoleDTO updateRole(RequestRoleDTO role,Long id);

    void deleteRole(Long id);
}
