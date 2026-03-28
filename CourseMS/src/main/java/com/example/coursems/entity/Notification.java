package com.example.coursems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private int notificationId;

    // FK → Users: Ai là người nhận thông báo này?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Nội dung thông báo (TEXT để không giới hạn)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    // Loại thông báo: NEW_COURSE, LESSON_UPDATED, ENROLLMENT_CONFIRMED, COURSE_COMPLETED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    // Đã đọc hay chưa? Mặc định = chưa đọc
    @Builder.Default
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    // Thời điểm tạo thông báo
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
}
