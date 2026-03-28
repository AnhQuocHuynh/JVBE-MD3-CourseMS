package com.example.coursems.repository;

import com.example.coursems.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByCourse_CourseId(int courseId);
    Optional<Review> findByCourse_CourseIdAndStudent_UserId(int courseId, int studentId);
    boolean existsByCourse_CourseIdAndStudent_UserId(int courseId, int studentId);
}
