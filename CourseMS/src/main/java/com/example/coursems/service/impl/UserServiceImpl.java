package com.example.coursems.service.impl;

import com.example.coursems.config.exception.BadRequestException;
import com.example.coursems.config.exception.DuplicateResourceException;
import com.example.coursems.config.exception.ForbiddenException;
import com.example.coursems.config.exception.ResourceNotFoundException;
import com.example.coursems.dto.request.UserCreateRequest;
import com.example.coursems.dto.request.UserPasswordRequest;
import com.example.coursems.dto.request.UserRoleRequest;
import com.example.coursems.dto.request.UserStatusRequest;
import com.example.coursems.dto.request.UserUpdateRequest;
import com.example.coursems.dto.response.UserResponse;
import com.example.coursems.entity.Role;
import com.example.coursems.entity.User;
import com.example.coursems.mapper.UserMapper;
import com.example.coursems.repository.UserRepository;
import com.example.coursems.service.UserService;
import com.example.coursems.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers(Role role, Boolean isActive) {
        return userRepository.findAll().stream()
                .filter(user -> role == null || user.getRole() == role)
                .filter(user -> isActive == null || user.isActive() == isActive)
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(int userId) {
        return userMapper.toResponse(getUserOrThrow(userId));
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username da ton tai.");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email da ton tai.");
        }
        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UserResponse updateUser(int userId, UserUpdateRequest request) {
        if (!securityUtil.isOwnerOrAdmin(userId)) {
            throw new ForbiddenException("Ban khong duoc phep cap nhat thong tin nguoi dung nay.");
        }
        User user = getUserOrThrow(userId);
        userRepository.findByEmail(request.getEmail())
                .filter(existing -> existing.getUserId() != userId)
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Email da ton tai.");
                });
        userMapper.updateEntity(request, user);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse updateRole(int userId, UserRoleRequest request) {
        User current = securityUtil.getCurrentUser();
        User target = getUserOrThrow(userId);
        if (target.getRole() == Role.ADMIN && (current == null || current.getUserId() != target.getUserId())) {
            throw new ForbiddenException("Khong duoc thay doi role cua ADMIN khac.");
        }
        userMapper.updateRole(request, target);
        return userMapper.toResponse(userRepository.save(target));
    }

    @Override
    @Transactional
    public UserResponse updateStatus(int userId, UserStatusRequest request) {
        User current = securityUtil.getCurrentUser();
        User target = getUserOrThrow(userId);
        if (target.getRole() == Role.ADMIN && (current == null || current.getUserId() != target.getUserId())) {
            throw new ForbiddenException("Khong duoc khoa/mo khoa tai khoan ADMIN khac.");
        }
        userMapper.updateStatus(request, target);
        return userMapper.toResponse(userRepository.save(target));
    }

    @Override
    @Transactional
    public void changePassword(int userId, UserPasswordRequest request) {
        if (!securityUtil.isOwnerOrAdmin(userId)) {
            throw new ForbiddenException("Ban khong duoc phep doi mat khau cua tai khoan nay.");
        }
        User user = getUserOrThrow(userId);
        if (securityUtil.getCurrentUserId() == userId &&
                !passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Mat khau hien tai khong dung.");
        }
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        User current = securityUtil.getCurrentUser();
        User target = getUserOrThrow(userId);
        if (target.getRole() == Role.ADMIN && (current == null || current.getUserId() != target.getUserId())) {
            throw new ForbiddenException("Khong duoc xoa tai khoan ADMIN khac.");
        }
        userRepository.delete(target);
    }

    private User getUserOrThrow(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung voi ID: " + userId));
    }
}
