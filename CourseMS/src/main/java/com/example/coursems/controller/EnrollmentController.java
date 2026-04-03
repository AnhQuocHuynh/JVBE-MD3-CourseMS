package com.example.coursems.controller;

import com.example.coursems.dto.request.EnrollmentRequest;
import com.example.coursems.dto.response.ApiResponse;
import com.example.coursems.dto.response.EnrollmentResponse;
import com.example.coursems.service.EnrollmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('STUDENT')")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @GetMapping
    public ApiResponse<List<EnrollmentResponse>> getMyEnrollments() {
        return ApiResponse.success("Lay danh sach ghi danh thanh cong", enrollmentService.getMyEnrollments());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EnrollmentResponse> enroll(@Valid @RequestBody EnrollmentRequest request) {
        return ApiResponse.success("Dang ky khoa hoc thanh cong", enrollmentService.enroll(request));
    }

    @GetMapping("/{enrollment_id}")
    public ApiResponse<EnrollmentResponse> getEnrollment(
            @PathVariable("enrollment_id") @Positive int enrollmentId) {
        return ApiResponse.success("Lay chi tiet tien do thanh cong", enrollmentService.getEnrollmentById(enrollmentId));
    }

    @PutMapping("/{enrollment_id}/complete_lesson/{lesson_id}")
    public ApiResponse<EnrollmentResponse> completeLesson(
            @PathVariable("enrollment_id") @Positive int enrollmentId,
            @PathVariable("lesson_id") @Positive int lessonId) {
        return ApiResponse.success("Cap nhat tien do bai hoc thanh cong", enrollmentService.completeLesson(enrollmentId, lessonId));
    }
}
