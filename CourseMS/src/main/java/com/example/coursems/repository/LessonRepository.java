package com.example.coursems.repository;

import com.example.coursems.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findByCourse_CourseIdOrderByOrderIndexAsc(int courseId);
    List<Lesson> findByCourse_CourseIdAndIsPublishedTrueOrderByOrderIndexAsc(int courseId);
}
