package com.message.auth_service.security_configuration;

import com.message.auth_service.config.RedisConfig;
import com.message.auth_service.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final RedisConfig redisConfig;

    @Autowired
    public JwtFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil, RedisConfig redisConfig){
        this.userDetailsService=userDetailsService;
        this.redisConfig=redisConfig;
        this.jwtUtil=jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            String jwToken = authorizationHeader.substring(7);
            try {
                if (Boolean.FALSE.equals(jwtUtil.isTokenExpired(jwToken))) {
                    String username = jwtUtil.extractUserName(jwToken);

                    try {
                        if (request.getServletPath().equals("/logout")) {
                            redisConfig.setValue(username, jwToken);
                            filterChain.doFilter(request, response);
                            return;
                        }
                        if (redisConfig.getValue(username).equals(jwToken)) {
                            filterChain.doFilter(request, response);
                            return;
                        }
                    } catch (Exception e) {
                        log.warn("Error while Checking Token in Redis Filter: {}", e.getMessage());
                    }

                    if (username != null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}