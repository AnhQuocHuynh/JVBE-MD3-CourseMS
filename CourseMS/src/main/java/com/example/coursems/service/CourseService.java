package com.example.coursems.service;

import com.example.coursems.dto.request.CourseRequest;
import com.example.coursems.dto.request.CourseStatusRequest;
import com.example.coursems.dto.response.CourseResponse;
import com.example.coursems.entity.CourseStatus;

import java.util.List;

public interface CourseService {
    List<CourseResponse> getAllCourses(String keyword, Integer teacherId, CourseStatus status);
    CourseResponse getCourseById(int courseId);
    CourseResponse createCourse(CourseRequest request);
    CourseResponse updateCourse(int courseId, CourseRequest request);
    CourseResponse updateStatus(int courseId, CourseStatusRequest request);
    void deleteCourse(int courseId);
}
