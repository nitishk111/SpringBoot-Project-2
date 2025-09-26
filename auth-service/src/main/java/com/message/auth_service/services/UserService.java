package com.message.auth_service.services;

import com.message.auth_service.dto.UserResponseDto;
import com.message.auth_service.dto.UserSignUpDto;

public interface UserService {

    UserResponseDto createUser(UserSignUpDto userDto);
    UserResponseDto getUserById(long userId) throws Exception;
    UserResponseDto getUser(String username) throws Exception;
    String updateUser(long userId, UserSignUpDto userDetails);
    void deleteUser(String username);
}
