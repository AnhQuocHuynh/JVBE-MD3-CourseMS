package com.example.coursems.mapper;

import com.example.coursems.dto.request.CourseRequest;
import com.example.coursems.dto.response.CourseResponse;
import com.example.coursems.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface CourseMapper {
    @Mapping(source = "teacher.userId", target = "teacherId")
    @Mapping(source = "teacher.fullName", target = "teacherName")
    CourseResponse toResponse(Course course);

    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    Course toEntity(CourseRequest request);

    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    void updateEntity(CourseRequest request, @MappingTarget Course course);
}
