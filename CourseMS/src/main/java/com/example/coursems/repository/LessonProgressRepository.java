package com.example.coursems.repository;

import com.example.coursems.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Integer> {
    List<LessonProgress> findByEnrollment_EnrollmentId(int enrollmentId);
    Optional<LessonProgress> findByEnrollment_EnrollmentIdAndLesson_LessonId(int enrollmentId, int lessonId);
    long countByEnrollment_EnrollmentIdAndIsCompletedTrue(int enrollmentId);
}
