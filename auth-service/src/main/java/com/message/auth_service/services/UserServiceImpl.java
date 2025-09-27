package com.message.auth_service.services;

import com.message.auth_service.dto.UserResponseDto;
import com.message.auth_service.dto.UserSignUpDto;
import com.message.auth_service.entity.PasswordOtp;
import com.message.auth_service.entity.User;
import com.message.auth_service.repository.PasswordOtpRepository;
import com.message.auth_service.repository.UserRepository;
import com.message.auth_service.util.IPValidUtil;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final PasswordOtpRepository otpRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper mapper, PasswordEncoder passwordEncoder, PasswordOtpRepository otpRepository) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.otpRepository=otpRepository;
    }

    @Override
    public UserResponseDto createUser(UserSignUpDto userDto) throws Exception {
        if(!IPValidUtil.validatePassword(userDto.getUserPassword()) || !IPValidUtil.validateUserName(userDto.getUserName())){
            throw new Exception("Invalid username and / or password");
        }
        User user = mapper.toEntity(userDto);
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        return mapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto getUserById(long userId) throws Exception {
        Optional<User> user = userRepository.findByUserIdAndIsActiveTrue(userId);
        if (user.isEmpty()) {
            log.warn("User does not exists");
            throw new RuntimeException("User does not exists");
        }
        return mapper.toDto(user.get());
    }

    @Override
    public UserResponseDto getUser(String username) throws Exception {
        Optional<User> user = userRepository.findByUserName(username);
        if (user.isEmpty()) {
            log.warn("User does not exists");
            throw new RuntimeException("User does not exists");
        }
        return mapper.toDto(user.get());
    }

    @Override
    @Transactional
    public String updateUser(long userId, UserSignUpDto userDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<User> update = cb.createCriteriaUpdate(User.class);
        Root<User> root = update.from(User.class);

        if (userDto.getUserName() != null) {
            if(IPValidUtil.validateUserName(userDto.getUserName())){
                return "Invalid username";
            }
            update.set("userName", userDto.getUserName());
        }
        if (userDto.getUserEmail() != null) {
            if(IPValidUtil.validateEmail(userDto.getUserEmail())){
                return "Invalid email";
            }
            update.set("userEmail", userDto.getUserEmail());
        }
        if (userDto.getUserPassword() != null) {
            if(IPValidUtil.validatePassword(userDto.getUserPassword())){
                return "Invalid password";
            }
            update.set("userPassword", passwordEncoder.encode(userDto.getUserPassword()));
        }
        update.where(cb.equal(root.get("userId"), userId));
        entityManager.createQuery(update).executeUpdate();
        return "Record updated";
    }

    @Override
    public void deleteUser(String username) {
        User existingUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found with id " + username));
        existingUser.setActive(false);
        userRepository.save(existingUser);
    }

    @Override
    public Integer generateOtp(String username) throws Exception {
        User user= userRepository.findByUserName(username).orElseThrow(()->new Exception("Incorrect username / email"));
        PasswordOtp passwordOtp = new PasswordOtp();
        passwordOtp.setUser(user);
        return otpRepository.save(passwordOtp).getOtp();
    }

    @Override
    public String resetPassword(int otp, String password, String username) throws Exception {
        if(! IPValidUtil.validatePassword(password)){
            return "Invalid new password";
        }
        User user= userRepository.findByUserName(username).orElseThrow(()->new Exception("Incorrect username / email"));
        List<PasswordOtp> passwordOtps= otpRepository.findByUser(user);
        for(PasswordOtp passotp: passwordOtps){
            if(passotp.getOtp()==otp && passotp.getExpiresAt().isAfter(LocalDateTime.now())&& passotp.isFlag()){
                user.setUserPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                return "Password Changes";
            }
        }
        throw new Exception("Incorrect OTP");
    }
}
