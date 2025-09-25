package com.message.auth_service;

import com.message.auth_service.entity.Role;
import com.message.auth_service.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminService {

    private final RoleRepository roleRepository;

    @Autowired
    public AdminService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role addNewRole(String roleType) throws Exception {
        Role role = new Role();
        role.setRoleType(roleType.strip().toUpperCase());
        try {
            role = roleRepository.save(role);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new Exception(e);
        }
        return role;
    }
}
