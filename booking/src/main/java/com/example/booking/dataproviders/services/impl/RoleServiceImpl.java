package com.example.booking.dataproviders.services.impl;

import com.example.booking.dataproviders.dto.roleDTOs.RequestRoleDTO;
import com.example.booking.dataproviders.dto.roleDTOs.ResponseRoleDTO;
import com.example.booking.dataproviders.services.RoleService;

import java.util.List;

public class RoleServiceImpl implements RoleService {
    @Override
    public List<ResponseRoleDTO> findAllRoles() {
        return List.of();
    }

    @Override
    public ResponseRoleDTO findRoleById(Long id) {
        return null;
    }

    @Override
    public ResponseRoleDTO saveRole(RequestRoleDTO role) {
        return null;
    }

    @Override
    public ResponseRoleDTO updateRole(RequestRoleDTO role, Long id) {
        return null;
    }

    @Override
    public void deleteRole(Long id) {

    }
}
