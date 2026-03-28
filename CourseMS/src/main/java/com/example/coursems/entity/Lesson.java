package com.example.coursems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lessons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private int lessonId;

    // Khóa ngoại → bảng Courses
    // FetchType.LAZY: Chỉ load Course khi thực sự cần (tối ưu hiệu năng)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // Tiêu đề bài học
    @Column(nullable = false, length = 255)
    private String title;

    // Link video/slide/tài liệu đính kèm
    @Column(name = "content_url", length = 500)
    private String contentUrl;

    // Nội dung văn bản của bài học
    @Column(name = "text_content", columnDefinition = "TEXT")
    private String textContent;

    // Thứ tự hiển thị trong khóa học (1, 2, 3, ...)
    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    // Trạng thái publish: true = học viên thấy được, false = còn ẩn
    @Builder.Default
    @Column(name = "is_published", nullable = false)
    private boolean isPublished = false;

    // ===== QUAN HỆ 1-N VỚI LessonProgress =====
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<LessonProgress> progressList;
}
