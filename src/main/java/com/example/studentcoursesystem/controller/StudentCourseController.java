package com.example.studentcoursesystem.controller;

import com.example.studentcoursesystem.model.StudentCourse;
import com.example.studentcoursesystem.service.StudentCourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-courses")
public class StudentCourseController {

    private final StudentCourseService studentCourseService;

    public StudentCourseController(StudentCourseService studentCourseService) {
        this.studentCourseService = studentCourseService;
    }

    @GetMapping
    public List<StudentCourse> getAllStudentCourses() {
        return studentCourseService.getAllStudentCourses();
    }

    @GetMapping("/student/{studentId}")
    public List<StudentCourse> getCoursesByStudentId(@PathVariable String studentId) {
        return studentCourseService.getCoursesByStudentId(studentId);
    }

    @GetMapping("/course/{courseId}")
    public List<StudentCourse> getStudentsByCourseId(@PathVariable String courseId) {
        return studentCourseService.getStudentsByCourseId(courseId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentCourse> getStudentCourseById(@PathVariable Integer id) {
        return studentCourseService.getStudentCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentCourse> selectCourse(@RequestBody StudentCourse studentCourse) {
        if (studentCourseService.selectCourse(studentCourse)) {
            return ResponseEntity.ok(studentCourse);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}/score")
    public ResponseEntity<StudentCourse> updateScore(@PathVariable Integer id, @RequestBody StudentCourse studentCourse) {
        studentCourse.setId(id);
        if (studentCourseService.updateScore(studentCourse)) {
            return ResponseEntity.ok(studentCourse);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> dropCourse(@PathVariable Integer id) {
        if (studentCourseService.dropCourse(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Void> dropCourse(
            @PathVariable String studentId,
            @PathVariable String courseId) {
        if (studentCourseService.dropCourse(studentId, courseId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}