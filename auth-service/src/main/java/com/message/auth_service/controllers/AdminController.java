package com.message.auth_service.controllers;
/**
 * Admin APIs
 */

import com.message.auth_service.services.AdminService;
import com.message.auth_service.entity.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Slf4j
@Tag(name= "3. Admin APIs", description = "Admin can add roles for users")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService){
        this.adminService=adminService;
    }

    @PostMapping("/add-new-role")
    @Operation(summary = "Add new role")
    public ResponseEntity<String> addNewRole(@RequestParam String roleType) throws Exception {
        log.info("Request for addition of new role.");
        Role role= adminService.addNewRole(roleType);
        log.info("New Role saved: {}", role);
        return new ResponseEntity<>(role.toString(),HttpStatus.ACCEPTED);
    }
}
