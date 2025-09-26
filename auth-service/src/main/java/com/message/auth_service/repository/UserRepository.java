package com.message.auth_service.repository;

import com.message.auth_service.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE (u.userName = :userName OR u.userEmail = :userName) AND u.isActive = true")
    public Optional<User> findByUserName(@Param("userName") String userName);

    public Optional<User> findByUserIdAndIsActiveTrue(long userId);
}
