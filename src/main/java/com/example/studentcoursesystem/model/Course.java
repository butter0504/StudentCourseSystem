package com.example.studentcoursesystem.model;

import lombok.Data;

@Data
public class Course {
    private String courseId;
    private String courseName;
    private Integer credit;
    private String teacher;
    private String classTime;
    private Integer capacity;
}