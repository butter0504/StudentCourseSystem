package com.example.studentcoursesystem.repository;

import com.example.studentcoursesystem.model.StudentCourse;

import java.util.List;
import java.util.Optional;

public interface StudentCourseRepository {
    List<StudentCourse> findAll();
    List<StudentCourse> findByStudentId(String studentId);
    List<StudentCourse> findByCourseId(String courseId);
    Optional<StudentCourse> findById(Integer id);
    int save(StudentCourse studentCourse);
    int updateScore(StudentCourse studentCourse);
    int deleteById(Integer id);
    int deleteByStudentAndCourse(String studentId, String courseId);
    boolean existsByStudentAndCourse(String studentId, String courseId);
}