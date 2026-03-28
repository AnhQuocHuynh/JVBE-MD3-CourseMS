package com.example.coursems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private int courseId;

    // Tiêu đề khóa học
    @Column(nullable = false, length = 255)
    private String title;

    // Mô tả nội dung (TEXT = không giới hạn ký tự)
    @Column(columnDefinition = "TEXT")
    private String description;

    // Khóa ngoại → bảng Users (phải là TEACHER)
    // @ManyToOne: Nhiều Course có thể thuộc 1 Teacher
    // @JoinColumn: Tên cột khóa ngoại trong DB là "teacher_id"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    // Giá khóa học, mặc định 0.00
    // BigDecimal dùng cho tiền tệ để tránh lỗi làm tròn của float/double
    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal price = BigDecimal.ZERO;

    // Tổng số giờ học
    @Column(name = "duration_hours")
    private int durationHours;

    // Trạng thái: DRAFT, PUBLISHED, ARCHIVED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CourseStatus status = CourseStatus.DRAFT;

    // Audit fields
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    // ===== QUAN HỆ 1-N VỚI CÁC BẢNG CON =====
    // mappedBy = "course": biến 'course' trong class Lesson là bên sở hữu khóa ngoại
    // cascade = REMOVE: Khi xóa Course thì tự xóa hết Lesson, Enrollment, Review thuộc nó
    // orphanRemoval = true: Nếu một Lesson bị remove khỏi list thì tự xóa trong DB

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
}
