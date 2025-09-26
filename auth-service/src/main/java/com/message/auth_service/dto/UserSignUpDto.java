package com.message.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignUpDto {

    @NotBlank(message = "username is mandatory")
    @Size(min=3, max = 10, message = "username should be between 3 to 10 character long")
//    @Email(message = "Enter Valid username, of length between 3 to 10", regexp = "^[A-Za-z][A-Za-z0-9_]{3,10}$")
    private String userName;

    @NotBlank(message = "email is mandatory")
    @Email(message = "Enter Valid email",regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String userEmail;

    @NotBlank(message = "password is mandatory")
    @Size(min = 6, max = 15, message = "password should be between 6 to 15 character long")
    private String userPassword;
}
