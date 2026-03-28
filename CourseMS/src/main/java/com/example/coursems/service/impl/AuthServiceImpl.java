package com.example.coursems.service.impl;

import com.example.coursems.config.JwtService;
import com.example.coursems.dto.AuthRequest;
import com.example.coursems.dto.AuthResponse;
import com.example.coursems.dto.RegisterRequest;
import com.example.coursems.dto.response.UserResponse;
import com.example.coursems.entity.User;
import com.example.coursems.repository.UserRepository;
import com.example.coursems.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isActive(true)
                .build();
        userRepository.save(user);
        var token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .message("Đăng ký thành công!")
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        // Hỗ trợ tìm bằng cả username hoặc email để login linh hoạt hơn
        var user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
        var token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .message("Xác thực thành công! Chào " + user.getFullName())
                .build();
    }

    @Override
    public boolean verifyToken() {
        return true;
    }

    @Override
    public UserResponse getCurrentUser() {
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Người dùng chưa được xác thực!");
        }

        User user = (User) authentication.getPrincipal();

        return com.example.coursems.dto.response.UserResponse.builder()
                .id(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .isActive(user.isActive())
                .build();
    }
}
