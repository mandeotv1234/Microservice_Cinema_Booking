package com.cinema.auth_service.service;

import com.cinema.auth_service.dto.AuthRequest;
import com.cinema.auth_service.dto.AuthResponse;
import com.cinema.auth_service.model.Role;
import com.cinema.auth_service.model.User;
import com.cinema.auth_service.repository.UserRepository;
import com.cinema.auth_service.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    @Autowired private UserRepository userRepo;
    @Autowired private JwtService jwtService;
    @Autowired private  PasswordEncoder passwordEncoder;
    public AuthResponse login(AuthRequest request) {
        User user = userRepo.findByUsername(request.getUsername());


        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }

    public void register(AuthRequest request) {
        User user = User.builder()
                .username(request.getUsername()) // ✅ username riêng
                .email(request.getEmail())       // ✅ email riêng
                .password(passwordEncoder.encode(request.getPassword())) // mã hóa password
                .role(Role.USER)
                .build();

        userRepo.save(user);
    }

}

