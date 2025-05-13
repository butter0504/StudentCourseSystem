package com.example.studentcoursesystem.service;

import com.example.studentcoursesystem.model.Student;
import com.example.studentcoursesystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(String studentId) {
        return studentRepository.findById(studentId);
    }

    public boolean addStudent(Student student) {
        return studentRepository.save(student) > 0;
    }

    public boolean updateStudent(Student student) {
        return studentRepository.update(student) > 0;
    }

    public boolean deleteStudent(String studentId) {
        return studentRepository.deleteById(studentId) > 0;
    }

    public Optional<Student> login(String studentId, String password) {
        return studentRepository.findByLogin(studentId, password);
    }
}