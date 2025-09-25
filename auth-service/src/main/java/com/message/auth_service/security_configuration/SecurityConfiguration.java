package com.message.auth_service.security_configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/test/a").hasRole("USER")
                                .requestMatchers("/test/b").hasRole("ADMIN")
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/v3/api-docs.yaml"
                                ).permitAll()
                                .anyRequest().permitAll())
                .httpBasic((Customizer.withDefaults())); // test in postman
//                .formLogin(Customizer.withDefaults()); // test in browser
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailService() {
        UserDetails user1 = User
                .withUsername("user1")
                .password(passwordEncoder().encode(("u1")))
                .roles("USER")
                .build();
        UserDetails user2 = User
                .withUsername("user2")
                .password(passwordEncoder().encode(("u2")))
                .roles("USER", "ADMIN")
                .build();
        UserDetails user3 = User
                .withUsername("user3")
                .password(passwordEncoder().encode(("u3")))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1, user2, user3);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
