package com.example.coursems.repository;

import com.example.coursems.entity.Course;
import com.example.coursems.entity.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByStatus(CourseStatus status);
    List<Course> findByTeacher_UserId(int teacherId);
    List<Course> findByTitleContainingIgnoreCase(String keyword);
    List<Course> findByTeacher_UserIdAndStatus(int teacherId, CourseStatus status);
    List<Course> findByTitleContainingIgnoreCaseAndStatus(String keyword, CourseStatus status);
}
