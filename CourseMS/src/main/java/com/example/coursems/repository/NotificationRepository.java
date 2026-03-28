package com.example.coursems.repository;

import com.example.coursems.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUser_UserIdOrderByCreatedAtDesc(int userId);
    long countByUser_UserIdAndIsReadFalse(int userId);
}
