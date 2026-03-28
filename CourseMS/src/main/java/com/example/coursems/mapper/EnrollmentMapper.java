package com.example.coursems.mapper;

import com.example.coursems.dto.response.EnrollmentResponse;
import com.example.coursems.dto.response.LessonProgressResponse;
import com.example.coursems.entity.Enrollment;
import com.example.coursems.entity.LessonProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    @Mapping(source = "student.userId", target = "studentId")
    @Mapping(source = "student.fullName", target = "studentName")
    @Mapping(source = "course.courseId", target = "courseId")
    @Mapping(source = "course.title", target = "courseTitle")
    @Mapping(source = "lessonProgressList", target = "lessonProgressList")
    EnrollmentResponse toResponse(Enrollment enrollment);

    @Mapping(source = "lesson.lessonId", target = "lessonId")
    @Mapping(source = "lesson.title", target = "lessonTitle")
    @Mapping(source = "completed", target = "isCompleted")
    LessonProgressResponse toProgressResponse(LessonProgress lessonProgress);
}
