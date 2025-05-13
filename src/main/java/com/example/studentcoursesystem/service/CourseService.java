package com.example.studentcoursesystem.service;

import com.example.studentcoursesystem.model.Course;
import com.example.studentcoursesystem.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(String courseId) {
        return courseRepository.findById(courseId);
    }

    public boolean addCourse(Course course) {
        return courseRepository.save(course) > 0;
    }

    public boolean updateCourse(Course course) {
        return courseRepository.update(course) > 0;
    }

    public boolean deleteCourse(String courseId) {
        return courseRepository.deleteById(courseId) > 0;
    }

    public List<Course> getAvailableCoursesForStudent(String studentId) {
        return courseRepository.findAvailableCoursesForStudent(studentId);
    }
}