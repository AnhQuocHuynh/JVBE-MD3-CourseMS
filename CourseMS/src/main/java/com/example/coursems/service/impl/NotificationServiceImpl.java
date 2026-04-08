package com.example.coursems.service.impl;

import com.example.coursems.config.exception.ForbiddenException;
import com.example.coursems.config.exception.ResourceNotFoundException;
import com.example.coursems.dto.request.NotificationCreateRequest;
import com.example.coursems.dto.response.NotificationResponse;
import com.example.coursems.entity.Notification;
import com.example.coursems.entity.User;
import com.example.coursems.repository.NotificationRepository;
import com.example.coursems.repository.UserRepository;
import com.example.coursems.service.NotificationService;
import com.example.coursems.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyNotifications() {
        User user = securityUtil.getCurrentUser();
        if (user == null) {
            throw new ForbiddenException("Ban chua dang nhap.");
        }
        return notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(user.getUserId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(int notificationId) {
        User user = securityUtil.getCurrentUser();
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay thong bao voi ID: " + notificationId));
        if (user == null || notification.getUser().getUserId() != user.getUserId()) {
            throw new ForbiddenException("Ban khong duoc phep cap nhat thong bao nay.");
        }
        notification.setRead(true);
        return toResponse(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public NotificationResponse createNotification(NotificationCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung voi ID: " + request.getUserId()));
        Notification notification = Notification.builder()
                .user(user)
                .type(request.getType())
                .message(request.getMessage())
                .isRead(false)
                .build();
        return toResponse(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public void deleteNotification(int notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay thong bao voi ID: " + notificationId));
        notificationRepository.delete(notification);
    }

    private NotificationResponse toResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setNotificationId(notification.getNotificationId());
        response.setMessage(notification.getMessage());
        response.setType(notification.getType());
        response.setRead(notification.isRead());
        response.setCreatedAt(notification.getCreatedAt());
        return response;
    }
}
