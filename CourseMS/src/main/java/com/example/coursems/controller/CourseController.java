package com.example.coursems.controller;

import com.example.coursems.dto.request.CourseRequest;
import com.example.coursems.dto.request.CourseStatusRequest;
import com.example.coursems.dto.response.ApiResponse;
import com.example.coursems.dto.response.CourseResponse;
import com.example.coursems.entity.CourseStatus;
import com.example.coursems.service.CourseService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
// Đây là kỹ thuật Field Injection hiện đại nhất:
// Dùng @RequiredArgsConstructor thay cho @Autowired dư thừa
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    /**
     * Lấy danh sách khóa học, ai cũng xem được miễn là đã đăng nhập (Quyền: AUTH)
     * Thường dành cho trang chủ tìm kiếm. Admin cũng dùng cái này nhưng không lọc status.
     */
    @GetMapping
    public ApiResponse<List<CourseResponse>> getAllCourses(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer teacher_id,
            @RequestParam(required = false) CourseStatus status) {

        return ApiResponse.<List<CourseResponse>>builder()
                .success(true)
                .message("Lấy danh sách khóa học thành công")
                .data(courseService.getAllCourses(search, teacher_id, status))
                .build();
    }

    /**
     * Xem chi tiết khóa học.
     */
    @GetMapping("/{course_id}")
    public ApiResponse<CourseResponse> getCourseById(@PathVariable int course_id) {
        return ApiResponse.<CourseResponse>builder()
                .success(true)
                .message("Lấy thông tin chi tiết khóa học thành công")
                .data(courseService.getCourseById(course_id))
                .build();
    }

    /**
     * Tạo khóa học mới.
     * @PreAuthorize("hasRole('ADMIN')") - Biện pháp bảo vệ vòng ngoài.
     * Filter JWT là trạm gác đầu tiên, @PreAuthorize là cánh cửa thép riêng của hàm này.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CourseResponse> createCourse(@Valid @RequestBody CourseRequest request) {
        return ApiResponse.<CourseResponse>builder()
                .success(true)
                .message("Tạo khóa học thành công")
                .data(courseService.createCourse(request))
                .build();
    }

    /**
     * Cập nhật toàn bộ thông tin khóa học (Quyền: ADMIN).
     */
    @PutMapping("/{course_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CourseResponse> updateCourse(
            @PathVariable int course_id, 
            @Valid @RequestBody CourseRequest request) {
                
        return ApiResponse.<CourseResponse>builder()
                .success(true)
                .message("Cập nhật thông tin khóa học thành công")
                .data(courseService.updateCourse(course_id, request))
                .build();
    }

    /**
     * Thay đổi trạng thái khóa học (DRAFT, PUBLISHED, ARCHIVED). (Quyền: ADMIN)
     */
    @PutMapping("/{course_id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CourseResponse> updateCourseStatus(
            @PathVariable int course_id, 
            @RequestBody CourseStatusRequest request) {
                
        return ApiResponse.<CourseResponse>builder()
                .success(true)
                .message("Cập nhật trạng thái khóa học thành công")
                .data(courseService.updateStatus(course_id, request))
                .build();
    }

    /**
     * Xóa hoàn toàn một khóa học. (Quyền: ADMIN)
     */
    @DeleteMapping("/{course_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteCourse(@PathVariable int course_id) {
        courseService.deleteCourse(course_id);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Xóa khóa học thành công")
                .build();
    }
}
