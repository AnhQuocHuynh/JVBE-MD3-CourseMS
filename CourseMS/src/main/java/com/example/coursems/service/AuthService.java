package com.example.coursems.service;

import com.example.coursems.dto.AuthRequest;
import com.example.coursems.dto.AuthResponse;
import com.example.coursems.dto.RegisterRequest;
import com.example.coursems.dto.response.UserResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);
    boolean verifyToken();
    UserResponse getCurrentUser();
}
