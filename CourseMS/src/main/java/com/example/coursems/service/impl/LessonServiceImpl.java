package com.example.coursems.service.impl;

import com.example.coursems.config.exception.ForbiddenException;
import com.example.coursems.config.exception.ResourceNotFoundException;
import com.example.coursems.dto.request.LessonRequest;
import com.example.coursems.dto.response.LessonResponse;
import com.example.coursems.entity.Course;
import com.example.coursems.entity.CourseStatus;
import com.example.coursems.entity.Lesson;
import com.example.coursems.mapper.LessonMapper;
import com.example.coursems.repository.CourseRepository;
import com.example.coursems.repository.LessonRepository;
import com.example.coursems.service.LessonService;
import com.example.coursems.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> getLessonsByCourse(int courseId) {
        Course course = getCourseOrThrow(courseId);
        List<Lesson> lessons = securityUtil.isTeacherOfCourseOrAdmin(courseId)
                ? lessonRepository.findByCourse_CourseIdOrderByOrderIndexAsc(courseId)
                : lessonRepository.findByCourse_CourseIdAndIsPublishedTrueOrderByOrderIndexAsc(courseId);
        if (!securityUtil.isTeacherOfCourseOrAdmin(courseId) && course.getStatus() != CourseStatus.PUBLISHED) {
            throw new ForbiddenException("Ban khong duoc phep xem bai hoc cua khoa hoc nay.");
        }
        return lessons.stream().map(lessonMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LessonResponse getLessonById(int lessonId) {
        Lesson lesson = getLessonOrThrow(lessonId);
        boolean canManageCourse = securityUtil.isTeacherOfCourseOrAdmin(lesson.getCourse().getCourseId());
        if (canManageCourse) {
            return lessonMapper.toResponse(lesson);
        }
        if (lesson.getCourse().getStatus() != CourseStatus.PUBLISHED || !lesson.isPublished()) {
            throw new ForbiddenException("Ban khong duoc phep xem bai hoc nay.");
        }
        return lessonMapper.toResponse(lesson);
    }

    @Override
    @Transactional
    public LessonResponse createLesson(int courseId, LessonRequest request) {
        Course course = getCourseOrThrow(courseId);
        requireTeacherOrAdminForCourse(course);
        Lesson lesson = lessonMapper.toEntity(request);
        lesson.setCourse(course);
        lesson.setPublished(false);
        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    public LessonResponse updateLesson(int lessonId, LessonRequest request) {
        Lesson lesson = getLessonOrThrow(lessonId);
        requireTeacherOrAdminForCourse(lesson.getCourse());
        lessonMapper.updateEntity(request, lesson);
        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    public LessonResponse togglePublish(int lessonId) {
        Lesson lesson = getLessonOrThrow(lessonId);
        requireTeacherOrAdminForCourse(lesson.getCourse());
        lesson.setPublished(!lesson.isPublished());
        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    public void deleteLesson(int lessonId) {
        Lesson lesson = getLessonOrThrow(lessonId);
        requireTeacherOrAdminForCourse(lesson.getCourse());
        lessonRepository.delete(lesson);
    }

    private Lesson getLessonOrThrow(int lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay bai hoc voi ID: " + lessonId));
    }

    private Course getCourseOrThrow(int courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay khoa hoc voi ID: " + courseId));
    }

    private void requireTeacherOrAdminForCourse(Course course) {
        if (securityUtil.isAdmin()) {
            return;
        }
        if (securityUtil.getCurrentUserId() != course.getTeacher().getUserId()) {
            throw new ForbiddenException("Ban khong duoc phep thao tac bai hoc cua khoa hoc nay.");
        }
    }
}
