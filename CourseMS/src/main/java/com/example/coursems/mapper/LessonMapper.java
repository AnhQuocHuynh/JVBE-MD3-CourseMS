package com.example.coursems.mapper;

import com.example.coursems.dto.request.LessonRequest;
import com.example.coursems.dto.response.LessonResponse;
import com.example.coursems.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    @Mapping(source = "course.courseId", target = "courseId")
    LessonResponse toResponse(Lesson lesson);

    @Mapping(target = "lessonId", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "isPublished", ignore = true)
    @Mapping(target = "progressList", ignore = true)
    Lesson toEntity(LessonRequest request);

    @Mapping(target = "lessonId", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "published", ignore = true)
    @Mapping(target = "progressList", ignore = true)
    void updateEntity(LessonRequest request, @MappingTarget Lesson lesson);
}
