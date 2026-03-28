package com.example.coursems.service;

import com.example.coursems.dto.request.EnrollmentRequest;
import com.example.coursems.dto.response.EnrollmentResponse;

import java.util.List;

public interface EnrollmentService {
    List<EnrollmentResponse> getMyEnrollments();
    EnrollmentResponse getEnrollmentById(int enrollmentId);
    EnrollmentResponse enroll(EnrollmentRequest request);
    EnrollmentResponse completeLesson(int enrollmentId, int lessonId);
}
