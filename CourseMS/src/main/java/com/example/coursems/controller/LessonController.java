package com.example.coursems.controller;

import com.example.coursems.dto.request.LessonRequest;
import com.example.coursems.dto.response.ApiResponse;
import com.example.coursems.dto.response.LessonResponse;
import com.example.coursems.service.LessonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/api/courses/{course_id}/lessons")
    public ApiResponse<List<LessonResponse>> getLessonsByCourse(
            @PathVariable("course_id") @Positive int courseId) {
        return ApiResponse.success("Lay danh sach bai hoc thanh cong", lessonService.getLessonsByCourse(courseId));
    }

    @GetMapping("/api/lessons/{lesson_id}")
    public ApiResponse<LessonResponse> getLessonById(
            @PathVariable("lesson_id") @Positive int lessonId) {
        return ApiResponse.success("Lay chi tiet bai hoc thanh cong", lessonService.getLessonById(lessonId));
    }

    @PostMapping("/api/courses/{course_id}/lessons")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<LessonResponse> createLesson(
            @PathVariable("course_id") @Positive int courseId,
            @Valid @RequestBody LessonRequest request) {
        return ApiResponse.success("Tao bai hoc thanh cong", lessonService.createLesson(courseId, request));
    }

    @PutMapping("/api/lessons/{lesson_id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<LessonResponse> updateLesson(
            @PathVariable("lesson_id") @Positive int lessonId,
            @Valid @RequestBody LessonRequest request) {
        return ApiResponse.success("Cap nhat bai hoc thanh cong", lessonService.updateLesson(lessonId, request));
    }

    @PutMapping("/api/lessons/{lesson_id}/publish")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<LessonResponse> togglePublish(
            @PathVariable("lesson_id") @Positive int lessonId) {
        return ApiResponse.success("Cap nhat trang thai hien thi bai hoc thanh cong", lessonService.togglePublish(lessonId));
    }

    @DeleteMapping("/api/lessons/{lesson_id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<Void> deleteLesson(
            @PathVariable("lesson_id") @Positive int lessonId) {
        lessonService.deleteLesson(lessonId);
        return ApiResponse.success("Xoa bai hoc thanh cong", null);
    }

    @GetMapping("/api/lessons/{lesson_id}/content_preview")
    public ApiResponse<String> previewContent(
            @PathVariable("lesson_id") @Positive int lessonId) {
        LessonResponse lesson = lessonService.getLessonById(lessonId);
        String content = lesson.getTextContent() == null ? "" : lesson.getTextContent();
        String preview = content.length() <= 120 ? content : content.substring(0, 120) + "...";
        return ApiResponse.success("Lay noi dung preview thanh cong", preview);
    }
}
