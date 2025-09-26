package com.message.auth_service.controllers;

import com.message.auth_service.dto.UserResponseDto;
import com.message.auth_service.dto.UserSignUpDto;
import com.message.auth_service.entity.User;
import com.message.auth_service.services.UserService;
import com.message.auth_service.services.UserServiceImpl;
import com.message.auth_service.util.JwtUtil;
import com.message.auth_service.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "2. User APIs", description = "User can update and delete their profile")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @Autowired
    public UserController(UserServiceImpl userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/{id}")
    @Operation(summary = "See profile of user by id")
    public ResponseEntity<Object> getUserById(@PathVariable("id") long userId) throws Exception {
        try {
            return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("User not found with id: " + userId, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    @Operation(summary = "See your profile")
    public ResponseEntity<Object> getUser() throws Exception {
        String username = SecurityUtils.getCurrentUser().getUsername();
        try {
            return new ResponseEntity<>(userService.getUser(username), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Problem encountered while fetching profile: " + e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    @PutMapping
    @Operation(summary = "Enter fields you want to update")
    public ResponseEntity<String> updateUser(@RequestParam(value = "current_password") String currentPassword,@RequestParam(value = "username", required = false) String userName, @RequestParam(value = "password", required = false) String password, @RequestParam(value = "email", required = false) String userEmail) throws Exception {
        String username = SecurityUtils.getCurrentUser().getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, currentPassword));

        long userId = userService.getUser(username).getUserId();
        UserSignUpDto user = new UserSignUpDto();
        user.setUserEmail(userEmail);
        user.setUserName(userName);
        user.setUserPassword(password);
        userService.updateUser(userId, user);
        return ResponseEntity.ok(jwtUtil.generateToken(user.getUserName() != null ? user.getUserName() : username));
    }

    @DeleteMapping("/{password}")
    @Operation(summary = "Delete your profile by entering password")
    public ResponseEntity<String> deleteUser(@PathVariable("password") String password) {
        String username = SecurityUtils.getCurrentUser().getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        try {
            userService.deleteUser(username);
            return new ResponseEntity<>("Profile Deleted", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>("Problem encountered while deleting profile: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

