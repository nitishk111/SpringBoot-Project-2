package com.message.auth_service.config;

import com.message.auth_service.entity.User;
import com.message.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("Email is not registered");
        return org.springframework.security.core.userdetails.User.withUsername(user.get().getUserName())
                .password(user.get().getUserPassword())
                .roles(user.get().getRole().getRoleType())
                .build();
    }


}