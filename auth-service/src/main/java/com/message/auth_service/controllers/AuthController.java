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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "2. Public Controller", description = "User can register or sign in")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserServiceImpl userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager= authenticationManager;
        this.jwtUtil=jwtUtil;
    }

    @PostMapping("/sign-up")
    @Operation(summary = "New Registration")
    public void signUp(@Validated @RequestBody UserSignUpDto userDto) {
        userService.createUser(userDto);
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Sign in")
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
}
