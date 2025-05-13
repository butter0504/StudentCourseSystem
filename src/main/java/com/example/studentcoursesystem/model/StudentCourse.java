package com.example.studentcoursesystem.model;

import lombok.Data;

@Data
public class StudentCourse {
    private Integer id;
    private String studentId;
    private String courseId;
    private Double score;
}