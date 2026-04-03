package com.example.coursems.controller;

import com.example.coursems.dto.request.CourseRequest;
import com.example.coursems.dto.request.CourseStatusRequest;
import com.example.coursems.dto.response.ApiResponse;
import com.example.coursems.dto.response.CourseResponse;
import com.example.coursems.dto.response.PaginatedData;
import com.example.coursems.entity.CourseStatus;
import com.example.coursems.service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Validated
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ApiResponse<PaginatedData<CourseResponse>> getAllCourses(
            @RequestParam(required = false) @Size(max = 255, message = "Tu khoa tim kiem khong duoc qua 255 ky tu") String search,
            @RequestParam(required = false) @Positive(message = "teacher_id phai lon hon 0") Integer teacher_id,
            @RequestParam(required = false) CourseStatus status,
            @RequestParam(defaultValue = "1") @Positive(message = "page phai lon hon 0") int page,
            @RequestParam(defaultValue = "10") @Positive(message = "size phai lon hon 0") int size) {
        List<CourseResponse> allItems = courseService.getAllCourses(search, teacher_id, status);
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        int fromIndex = Math.min((safePage - 1) * safeSize, allItems.size());
        int toIndex = Math.min(fromIndex + safeSize, allItems.size());
        List<CourseResponse> currentItems = fromIndex >= toIndex
                ? Collections.emptyList()
                : allItems.subList(fromIndex, toIndex);

        return ApiResponse.successPage(
                "Lay danh sach khoa hoc thanh cong",
                currentItems,
                safePage,
                safeSize,
                allItems.size()
        );
    }

    @GetMapping("/{course_id}")
    public ApiResponse<CourseResponse> getCourseById(
            @PathVariable @Positive(message = "course_id phai lon hon 0") int course_id) {
        return ApiResponse.success(
                "Lay thong tin chi tiet khoa hoc thanh cong",
                courseService.getCourseById(course_id)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CourseResponse> createCourse(@Valid @RequestBody CourseRequest request) {
        return ApiResponse.success("Tao khoa hoc thanh cong", courseService.createCourse(request));
    }

    @PutMapping("/{course_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CourseResponse> updateCourse(
            @PathVariable @Positive(message = "course_id phai lon hon 0") int course_id,
            @Valid @RequestBody CourseRequest request) {
        return ApiResponse.success(
                "Cap nhat thong tin khoa hoc thanh cong",
                courseService.updateCourse(course_id, request)
        );
    }

    @PutMapping("/{course_id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CourseResponse> updateCourseStatus(
            @PathVariable @Positive(message = "course_id phai lon hon 0") int course_id,
            @Valid @RequestBody CourseStatusRequest request) {
        return ApiResponse.success(
                "Cap nhat trang thai khoa hoc thanh cong",
                courseService.updateStatus(course_id, request)
        );
    }

    @DeleteMapping("/{course_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteCourse(
            @PathVariable @Positive(message = "course_id phai lon hon 0") int course_id) {
        courseService.deleteCourse(course_id);
        return ApiResponse.success("Xoa khoa hoc thanh cong", null);
    }
}
