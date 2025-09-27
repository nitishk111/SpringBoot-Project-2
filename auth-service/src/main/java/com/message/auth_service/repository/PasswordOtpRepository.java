package com.message.auth_service.repository;

import com.message.auth_service.entity.PasswordOtp;
import com.message.auth_service.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PasswordOtpRepository extends JpaRepository<PasswordOtp, Long> {

    @Query("SELECT u FROM PasswordOtp u WHERE u.user = :user")
    List<PasswordOtp> findByUser(@Param("user") User user);
}
