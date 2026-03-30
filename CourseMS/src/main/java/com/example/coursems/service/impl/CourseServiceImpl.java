package com.example.coursems.service.impl;

import com.example.coursems.service.CourseService;

import com.example.coursems.dto.request.CourseRequest;
import com.example.coursems.dto.request.CourseStatusRequest;
import com.example.coursems.dto.response.CourseResponse;
import com.example.coursems.entity.CourseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    @Override
    public List<CourseResponse> getAllCourses(String keyword, Integer teacherId, CourseStatus status) {
        return List.of();
    }

    @Override
    public CourseResponse getCourseById(int courseId) {
        return null;
    }

    @Override
    public CourseResponse createCourse(CourseRequest request) {
        return null;
    }

    @Override
    public CourseResponse updateCourse(int courseId, CourseRequest request) {
        return null;
    }

    @Override
    public CourseResponse updateStatus(int courseId, CourseStatusRequest request) {
        return null;
    }

    @Override
    public void deleteCourse(int courseId) {
    }
}
