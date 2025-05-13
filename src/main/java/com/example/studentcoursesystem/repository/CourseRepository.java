package com.example.studentcoursesystem.repository;

import com.example.studentcoursesystem.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    List<Course> findAll();
    Optional<Course> findById(String courseId);
    int save(Course course);
    int update(Course course);
    int deleteById(String courseId);
    List<Course> findAvailableCoursesForStudent(String studentId);
}