package com.example.coursems.mapper;

import com.example.coursems.dto.response.LessonResponse;
import com.example.coursems.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    @Mapping(source = "course.courseId", target = "courseId")
    @Mapping(source = "published", target = "published")
    LessonResponse toResponse(Lesson lesson);
}
