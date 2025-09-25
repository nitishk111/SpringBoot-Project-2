package com.message.auth_service.controllers;

import com.message.auth_service.dto.UserSignInDto;
import com.message.auth_service.dto.UserSignUpDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name="2. Public Controller", description = "User can register or sign in")
public class AuthController {

    @PostMapping("/sign-up")
    @Operation(summary = "New Registration")
    public void signUp(@Validated @RequestBody UserSignUpDto userDto){

    }

    @PostMapping("/sign-in")
    @Operation(summary = "Sign in")
    public void signIn(@Validated @RequestBody UserSignInDto userDto){

    }
}
