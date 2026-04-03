package com.example.coursems.service.impl;

import com.example.coursems.config.JwtService;
import com.example.coursems.config.TokenBlacklistService;
import com.example.coursems.config.exception.BadRequestException;
import com.example.coursems.config.exception.DuplicateResourceException;
import com.example.coursems.dto.request.AuthRequest;
import com.example.coursems.dto.request.RegisterRequest;
import com.example.coursems.dto.response.AuthResponse;
import com.example.coursems.dto.response.UserResponse;
import com.example.coursems.entity.Role;
import com.example.coursems.entity.User;
import io.jsonwebtoken.JwtException;
import com.example.coursems.mapper.UserMapper;
import com.example.coursems.repository.UserRepository;
import com.example.coursems.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email da ton tai.");
        }
        String username = request.getEmail().trim().toLowerCase();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicateResourceException("Username da ton tai.");
        }
        User user = User.builder()
                .username(username)
                .fullName(request.getFullName())
                .email(request.getEmail().trim().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .isActive(true)
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .message("Dang ky thanh cong!")
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new BadRequestException("Tai khoan khong ton tai."));
        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .message("Dang nhap thanh cong!")
                .build();
    }

    @Override
    public boolean verifyToken() {
        return true;
    }

    @Override
    public boolean logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadRequestException("Authorization header khong hop le.");
        }
        String token = authHeader.substring(7);
        try {
            tokenBlacklistService.blacklist(token, jwtService.extractExpirationDate(token).getTime());
        } catch (JwtException | IllegalArgumentException ex) {
            throw new BadRequestException("Token khong hop le.");
        }
        SecurityContextHolder.clearContext();
        return true;
    }

    @Override
    public UserResponse getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new BadRequestException("Khong tim thay thong tin nguoi dung hien tai.");
        }
        return userMapper.toResponse(user);
    }
}
