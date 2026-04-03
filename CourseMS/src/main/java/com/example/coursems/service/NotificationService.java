package com.example.coursems.service;

import com.example.coursems.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getMyNotifications();
    NotificationResponse markAsRead(int notificationId);
}
