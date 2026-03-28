package com.example.coursems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
// Ràng buộc UNIQUE(student_id, course_id): Mỗi sinh viên chỉ đăng ký 1 lần / khóa học
@Table(name = "enrollments", uniqueConstraints = {
        @UniqueConstraint(name = "uk_enrollment_student_course", columnNames = {"student_id", "course_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private int enrollmentId;

    // Sinh viên đăng ký (FK → Users, phải có role STUDENT)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    // Khóa học được đăng ký (FK → Courses)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // Ngày đăng ký (tự điền bằng @CreatedDate)
    @CreatedDate
    @Column(name = "enrollment_date", nullable = false, updatable = false)
    private Date enrollmentDate;

    // Trạng thái: ENROLLED, COMPLETED, DROPPED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.ENROLLED;

    // % Tiến độ hoàn thành (0.00 → 100.00)
    // DECIMAL(5,2): Tối đa 999.99 — đủ để chứa 0-100
    @Column(name = "progress_percentage", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal progressPercentage = BigDecimal.ZERO;

    // ===== QUAN HỆ 1-N VỚI LessonProgress =====
    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonProgress> lessonProgressList;
}
