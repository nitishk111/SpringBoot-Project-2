package com.message.auth_service.services;

import com.message.auth_service.dto.UserResponseDto;
import com.message.auth_service.dto.UserSignUpDto;
import com.message.auth_service.entity.User;

public interface UserService {

    User createUser(UserSignUpDto userDto);
    UserResponseDto getUserById(long userId) throws Exception;
    int updateUser(long userId, UserSignUpDto userDetails);
    void deleteUser(long userId);
}
