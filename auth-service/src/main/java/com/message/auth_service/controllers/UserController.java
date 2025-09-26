package com.message.auth_service.controllers;

import com.message.auth_service.dto.UserResponseDto;
import com.message.auth_service.dto.UserSignUpDto;
import com.message.auth_service.entity.User;
import com.message.auth_service.services.UserService;
import com.message.auth_service.services.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name= "3. User APIs", description = "User can update and delete their profile")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService){
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "See you profile")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") long userId) throws Exception {
        return new ResponseEntity<>(userService.getUserById(userId),HttpStatus.FOUND);
    }

    @PutMapping
    @Operation(summary = "Enter fields you want to update")
    public ResponseEntity<Integer> updateUser(@RequestParam(value = "id",required = false) long userId, @RequestParam(value = "username",required = false) String userName, @RequestParam(value = "password",required = false) String password, @RequestParam(value = "email",required = false) String userEmail) {
        UserSignUpDto user= new UserSignUpDto();
        user.setUserEmail(userEmail);
        user.setUserName(userName);
        user.setUserPassword(password);
        int columnsUpdated = userService.updateUser(userId, user);
        return ResponseEntity.ok(columnsUpdated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete your profile")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}

