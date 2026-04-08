package com.example.coursems.service.impl;

import com.example.coursems.config.exception.ResourceNotFoundException;
import com.example.coursems.config.exception.ForbiddenException;
import com.example.coursems.dto.request.CourseRequest;
import com.example.coursems.dto.request.CourseStatusRequest;
import com.example.coursems.dto.response.CourseResponse;
import com.example.coursems.dto.response.LessonResponse;
import com.example.coursems.entity.Course;
import com.example.coursems.entity.CourseStatus;
import com.example.coursems.entity.Role;
import com.example.coursems.entity.User;
import com.example.coursems.mapper.CourseMapper;
import com.example.coursems.repository.CourseRepository;
import com.example.coursems.repository.UserRepository;
import com.example.coursems.service.CourseService;
import com.example.coursems.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses(String keyword, Integer teacherId, CourseStatus status) {
        List<Course> courses;
        boolean admin = securityUtil.isAdmin();

        if (teacherId != null && status != null) {
            courses = courseRepository.findByTeacher_UserIdAndStatus(teacherId, status);
        } else if (keyword != null && status != null) {
            courses = courseRepository.findByTitleContainingIgnoreCaseAndStatus(keyword, status);
        } else if (teacherId != null) {
            courses = courseRepository.findByTeacher_UserId(teacherId);
        } else if (status != null) {
            courses = courseRepository.findByStatus(status);
        } else if (keyword != null) {
            courses = courseRepository.findByTitleContainingIgnoreCase(keyword);
        } else {
            courses = courseRepository.findAll();
        }

        return courses.stream()
                .filter(c -> admin || c.getStatus() == CourseStatus.PUBLISHED || c.getTeacher().getUserId() == securityUtil.getCurrentUserId())
                .filter(c -> keyword == null || c.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .filter(c -> teacherId == null || c.getTeacher().getUserId() == teacherId)
                .map(this::toResponseByViewer)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourseById(int courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay khoa hoc voi ID: " + courseId));
        if (!securityUtil.isAdmin()
                && course.getStatus() != CourseStatus.PUBLISHED
                && !securityUtil.isTeacherOfCourseOrAdmin(courseId)) {
            throw new ForbiddenException("Ban khong duoc phep xem khoa hoc nay.");
        }
        return toResponseByViewer(course);
    }

    @Override
    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        User teacher = getTeacherOrThrow(request.getTeacherId());
        Course course = courseMapper.toEntity(request);
        course.setTeacher(teacher);
        course.setStatus(CourseStatus.DRAFT);

        Course savedCourse = courseRepository.save(course);
        return courseMapper.toResponse(savedCourse);
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(int courseId, CourseRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay khoa hoc voi ID: " + courseId));
        User teacher = getTeacherOrThrow(request.getTeacherId());

        courseMapper.updateEntity(request, course);
        course.setTeacher(teacher);

        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toResponse(updatedCourse);
    }

    @Override
    @Transactional
    public CourseResponse updateStatus(int courseId, CourseStatusRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay khoa hoc voi ID: " + courseId));
        course.setStatus(request.getStatus());

        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toResponse(updatedCourse);
    }

    @Override
    @Transactional
    public void deleteCourse(int courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay khoa hoc voi ID: " + courseId));
        courseRepository.delete(course);
    }

    private User getTeacherOrThrow(Integer teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung voi ID: " + teacherId));

        if (teacher.getRole() != Role.TEACHER) {
            throw new IllegalArgumentException("Nguoi dung duoc chi dinh khong phai la giang vien (TEACHER).");
        }
        return teacher;
    }

    private CourseResponse toResponseByViewer(Course course) {
        CourseResponse response = courseMapper.toResponse(course);
        if (response.getLessons() == null) {
            return response;
        }
        boolean canViewAllLessons = securityUtil.isAdmin() || course.getTeacher().getUserId() == securityUtil.getCurrentUserId();
        if (!canViewAllLessons) {
            List<LessonResponse> visibleLessons = response.getLessons().stream()
                    .filter(Objects::nonNull)
                    .filter(LessonResponse::isPublished)
                    .toList();
            response.setLessons(visibleLessons);
        }
        return response;
    }
}
