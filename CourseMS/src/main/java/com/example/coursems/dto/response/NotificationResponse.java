package com.example.coursems.dto.response;

import com.example.coursems.entity.NotificationType;
import lombok.Data;

import java.util.Date;

@Data
public class NotificationResponse {
    private int notificationId;
    private String message;
    private NotificationType type;
    private boolean isRead;
    private Date createdAt;
}
