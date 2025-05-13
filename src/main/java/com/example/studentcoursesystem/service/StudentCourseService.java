package com.example.studentcoursesystem.service;

import com.example.studentcoursesystem.model.StudentCourse;
import com.example.studentcoursesystem.repository.StudentCourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentCourseService {

    private final StudentCourseRepository studentCourseRepository;

    public StudentCourseService(StudentCourseRepository studentCourseRepository) {
        this.studentCourseRepository = studentCourseRepository;
    }

    public List<StudentCourse> getAllStudentCourses() {
        return studentCourseRepository.findAll();
    }

    public List<StudentCourse> getCoursesByStudentId(String studentId) {
        return studentCourseRepository.findByStudentId(studentId);
    }

    public List<StudentCourse> getStudentsByCourseId(String courseId) {
        return studentCourseRepository.findByCourseId(courseId);
    }

    public Optional<StudentCourse> getStudentCourseById(Integer id) {
        return studentCourseRepository.findById(id);
    }

    public boolean selectCourse(StudentCourse studentCourse) {
        if (studentCourseRepository.existsByStudentAndCourse(
                studentCourse.getStudentId(), studentCourse.getCourseId())) {
            return false; // 已经选过该课程
        }
        return studentCourseRepository.save(studentCourse) > 0;
    }

    public boolean updateScore(StudentCourse studentCourse) {
        return studentCourseRepository.updateScore(studentCourse) > 0;
    }

    public boolean dropCourse(Integer id) {
        return studentCourseRepository.deleteById(id) > 0;
    }

    public boolean dropCourse(String studentId, String courseId) {
        return studentCourseRepository.deleteByStudentAndCourse(studentId, courseId) > 0;
    }
}