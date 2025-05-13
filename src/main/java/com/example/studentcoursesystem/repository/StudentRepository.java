package com.example.studentcoursesystem.repository;

import com.example.studentcoursesystem.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    List<Student> findAll();
    Optional<Student> findById(String studentId);
    int save(Student student);
    int update(Student student);
    int deleteById(String studentId);
    Optional<Student> findByLogin(String studentId, String password);
}