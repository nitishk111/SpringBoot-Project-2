package com.message.auth_service.dto;

import com.message.auth_service.util.IPValidUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignUpDto {

    @NotBlank(message = "username is mandatory")
    @Size(min=3, max = 10, message = "username should be between 3 to 10 character long")
    private String userName;

    @NotBlank(message = "email is mandatory")
    @Email(message = "Enter Valid email",regexp = IPValidUtil.email_regex)
    private String userEmail;

    @NotBlank(message = "password is mandatory")
    @Size(min = 6, max = 15, message = "password should be between 6 to 15 character long")
    private String userPassword;
}
