package com.example.studentcoursesystem.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Student {
    private String studentId;
    private String studentName;
    private String gender;
    private LocalDate birthDate;
    private String major;
    private String password;
}