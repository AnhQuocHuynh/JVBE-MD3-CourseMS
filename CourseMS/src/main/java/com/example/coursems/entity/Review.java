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
// Ràng buộc UNIQUE(course_id, student_id): Mỗi sinh viên chỉ được đánh giá 1 lần / khóa học
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(name = "uk_review_course_student", columnNames = {"course_id", "student_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int reviewId;

    // FK → Courses: Khóa học được đánh giá
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // FK → Users: Sinh viên viết review (phải có role STUDENT)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    // Số sao từ 1 đến 5
    // @Column(nullable = false) + validation sẽ kiểm tra phạm vi ở tầng Service
    @Column(nullable = false)
    private int rating;

    // Nội dung nhận xét chi tiết (có thể null nếu chỉ để sao)
    @Column(columnDefinition = "TEXT")
    private String comment;

    // Thời điểm tạo đánh giá
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
}
