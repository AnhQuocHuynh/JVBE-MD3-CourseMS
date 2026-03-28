package com.example.coursems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "lesson_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private int progressId;

    // FK → Enrollments: Tiến độ này thuộc về lượt đăng ký học nào?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    // FK → Lessons: Bài học nào đang được theo dõi?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    // Sinh viên đã hoàn thành bài học này chưa?
    @Builder.Default
    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted = false;

    // Thời điểm hoàn thành (null nếu chưa xong)
    @Column(name = "completed_at")
    private Date completedAt;

    // Lần cuối cùng truy cập bài học này
    @Column(name = "last_accessed_at")
    private Date lastAccessedAt;
}
