package com.example.coursems.service.impl;

import com.example.coursems.config.exception.ResourceNotFoundException;
import com.example.coursems.dto.request.CourseRequest;
import com.example.coursems.dto.request.CourseStatusRequest;
import com.example.coursems.dto.response.CourseResponse;
import com.example.coursems.entity.Course;
import com.example.coursems.entity.CourseStatus;
import com.example.coursems.entity.Role;
import com.example.coursems.entity.User;
import com.example.coursems.mapper.CourseMapper;
import com.example.coursems.repository.CourseRepository;
import com.example.coursems.repository.UserRepository;
import com.example.coursems.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;

    // Đánh dấu Transactional(readOnly = true) cho các hàm chỉ đọc
    // Giúp Hibernate tối ưu hóa session, không lưu snapshot
    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses(String keyword, Integer teacherId, CourseStatus status) {
        List<Course> courses;

        // Xử lý logic query method cơ bản (Đối với hệ thống lớn hơn ta dùng Specification)
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

        // Dùng Stream API để lọc thêm nếu các query DB chưa cover hết logic lọc kết hợp
        return courses.stream()
                .filter(c -> keyword == null || c.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .filter(c -> teacherId == null || c.getTeacher().getUserId() == teacherId)
                .map(courseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourseById(int courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học với ID: " + courseId));
        return courseMapper.toResponse(course);
    }

    @Override
    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        // Kiểm tra xem User được gán có tồn tại và có Role TEACHER không
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + request.getTeacherId()));

        if (teacher.getRole() != Role.TEACHER) {
            throw new IllegalArgumentException("Người dùng được chỉ định không phải là Giảng viên (TEACHER)!");
        }

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .teacher(teacher)
                .price(request.getPrice())
                .durationHours(request.getDurationHours())
                .status(CourseStatus.DRAFT) // Khóa học mới tạo mặc định là nháp
                .build();

        Course savedCourse = courseRepository.save(course);
        return courseMapper.toResponse(savedCourse);
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(int courseId, CourseRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học với ID: " + courseId));

        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + request.getTeacherId()));

        if (teacher.getRole() != Role.TEACHER) {
            throw new IllegalArgumentException("Người dùng được chỉ định không phải là Giảng viên (TEACHER)!");
        }

        // Cập nhật thông tin
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setTeacher(teacher);
        course.setPrice(request.getPrice());
        course.setDurationHours(request.getDurationHours());

        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toResponse(updatedCourse);
    }

    @Override
    @Transactional
    public CourseResponse updateStatus(int courseId, CourseStatusRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học với ID: " + courseId));

        course.setStatus(request.getStatus());

        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toResponse(updatedCourse);
    }

    @Override
    @Transactional
    public void deleteCourse(int courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học với ID: " + courseId));
        
        // Nhờ CascadeType.ALL ở Entity, thao tác này sẽ tự xóa lessons và enrollments liên quan
        courseRepository.delete(course);
    }
}
