package com.message.auth_service.controllers;

import com.message.auth_service.dto.UserSignInDto;
import com.message.auth_service.dto.UserSignUpDto;
import com.message.auth_service.services.UserService;
import com.message.auth_service.services.UserServiceImpl;
import com.message.auth_service.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Tag(name = "1. Public APIs", description = "User can register or sign in")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserServiceImpl userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/test")
    @Operation(summary = "Returns message if service is up and responsive")
    public String sayHello() {
        return "Service Working..";
    }

    @PostMapping("/sign-up")
    @Operation(summary = "New Registration")
    public ResponseEntity<Object> signUp(@Validated @RequestBody UserSignUpDto userDto) {
        try {
            return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>("Can not save user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Sign in via username / email")
    public ResponseEntity<String> signIn(@Validated @RequestBody UserSignInDto userDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getUserPassword()));
            return new ResponseEntity<>(
                    jwtUtil.generateToken(userDto.getUserName()),
                    HttpStatus.ACCEPTED
            );
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot Password? Change it by entering email or username")
    public ResponseEntity<String> forgotPassword(@RequestParam(value = "username") String username) {
        try {
            return new ResponseEntity<>("OTP is: " + userService.generateOtp(username), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/Change-password")
    @Operation(summary = "Provide OTP and new password")
    public ResponseEntity<String> changePassword(@RequestParam(value = "username") String username, @RequestParam(value = "otp") int otp, @RequestParam(value = "password") String password) {
        try {
            return new ResponseEntity<>(userService.resetPassword(otp, password, username), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
