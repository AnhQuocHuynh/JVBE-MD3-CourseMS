package com.example.coursems.controller;

import com.example.coursems.dto.AuthRequest;
import com.example.coursems.dto.AuthResponse;
import com.example.coursems.dto.RegisterRequest;
import com.example.coursems.dto.response.ApiResponse;
import com.example.coursems.dto.response.UserResponse;
import com.example.coursems.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Đăng ký thành công!")
                .data(authService.register(registerRequest))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Đăng nhập thành công!")
                .data(authService.authenticate(authRequest))
                .build();
    }

    @PostMapping("/verify")
    public ApiResponse<Boolean> verify() {
        return ApiResponse.<Boolean>builder()
                .success(true)
                .message("Token còn hiệu lực!")
                .data(authService.verifyToken())
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> me() {
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("Lấy thông tin profile thành công!")
                .data(authService.getCurrentUser())
                .build();
    }
}
