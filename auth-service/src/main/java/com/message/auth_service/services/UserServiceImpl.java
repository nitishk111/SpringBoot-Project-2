package com.message.auth_service.services;

import com.message.auth_service.dto.UserResponseDto;
import com.message.auth_service.dto.UserSignUpDto;
import com.message.auth_service.entity.User;
import com.message.auth_service.repository.UserRepository;
import com.message.auth_service.util.UserMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper mapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public User createUser(UserSignUpDto userDto) {
        User user = mapper.toEntity(userDto);
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserResponseDto getUserById(long userId) throws Exception {
        Optional<User> user= userRepository.findById(userId);
        if(user.isEmpty()){
            log.warn("User does not exists");
            throw new RuntimeException("User does not exists");
        }
        return mapper.toDto(user.get());
    }

    @Override
    @Transactional
    public int updateUser(long userId, UserSignUpDto userDetail) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<User> update = cb.createCriteriaUpdate(User.class);
        Root<User> root = update.from(User.class);

        if (userDetail.getUserName() != null) {
            update.set("userName", userDetail.getUserName());
        }
        if (userDetail.getUserEmail() != null) {
            update.set("userEmail", userDetail.getUserEmail());
        }
        if (userDetail.getUserPassword() != null) {
            update.set("userEmail", passwordEncoder.encode(userDetail.getUserPassword()));
        }

        update.where(cb.equal(root.get("userId"), userId));
        return entityManager.createQuery(update).executeUpdate();
    }

    @Override
    public void deleteUser(long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        existingUser.setActive(false);
        userRepository.save(existingUser);
    }
}
