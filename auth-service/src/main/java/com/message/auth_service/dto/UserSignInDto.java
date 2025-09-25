package com.message.auth_service.dto;

import lombok.Data;

@Data
public class UserSignInDto {
    private String userName;
    private String userPassword;
}
