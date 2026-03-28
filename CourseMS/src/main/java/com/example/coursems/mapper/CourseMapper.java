package com.example.coursems.mapper;

import com.example.coursems.dto.response.CourseResponse;
import com.example.coursems.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface CourseMapper {
    @Mapping(source = "teacher.userId", target = "teacherId")
    @Mapping(source = "teacher.fullName", target = "teacherName")
    CourseResponse toResponse(Course course);
}
