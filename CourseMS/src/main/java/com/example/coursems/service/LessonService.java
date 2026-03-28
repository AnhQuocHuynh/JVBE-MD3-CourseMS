package com.example.coursems.service;

import com.example.coursems.dto.request.LessonRequest;
import com.example.coursems.dto.response.LessonResponse;

import java.util.List;

public interface LessonService {
    List<LessonResponse> getLessonsByCourse(int courseId);
    LessonResponse getLessonById(int lessonId);
    LessonResponse createLesson(int courseId, LessonRequest request);
    LessonResponse updateLesson(int lessonId, LessonRequest request);
    LessonResponse togglePublish(int lessonId);
    void deleteLesson(int lessonId);
}
