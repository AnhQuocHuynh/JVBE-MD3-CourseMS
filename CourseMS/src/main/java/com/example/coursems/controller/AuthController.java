package com.example.coursems.controller;

import com.example.coursems.dto.request.AuthRequest;
import com.example.coursems.dto.request.RegisterRequest;
import com.example.coursems.dto.response.ApiResponse;
import com.example.coursems.dto.response.AuthResponse;
import com.example.coursems.dto.response.UserResponse;
import jakarta.validation.Valid;
import com.example.coursems.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ApiResponse.success("Dang ky thanh cong!", authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        return ApiResponse.success("Dang nhap thanh cong!", authService.authenticate(authRequest));
    }

    @PostMapping("/verify")
    public ApiResponse<Boolean> verify() {
        return ApiResponse.success("Token con hieu luc!", authService.verifyToken());
    }

    @PostMapping("/logout")
    public ApiResponse<Boolean> logout(@RequestHeader("Authorization") String authHeader) {
        return ApiResponse.success("Dang xuat thanh cong!", authService.logout(authHeader));
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> me() {
        return ApiResponse.success("Lay thong tin profile thanh cong!", authService.getCurrentUser());
    }
}
