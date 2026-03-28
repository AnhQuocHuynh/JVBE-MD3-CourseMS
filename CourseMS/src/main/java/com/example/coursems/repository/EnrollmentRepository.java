package com.example.coursems.repository;

import com.example.coursems.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByStudent_UserId(int studentId);
    Optional<Enrollment> findByStudent_UserIdAndCourse_CourseId(int studentId, int courseId);
    boolean existsByStudent_UserIdAndCourse_CourseId(int studentId, int courseId);
    List<Enrollment> findByCourse_CourseId(int courseId);
}
