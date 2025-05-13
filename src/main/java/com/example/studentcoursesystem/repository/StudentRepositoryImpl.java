package com.example.studentcoursesystem.repository;

import com.example.studentcoursesystem.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Student> studentRowMapper = (rs, rowNum) -> {
        Student student = new Student();
        student.setStudentId(rs.getString("student_id"));
        student.setStudentName(rs.getString("student_name"));
        student.setGender(rs.getString("gender"));
        student.setBirthDate(rs.getDate("birth_date").toLocalDate());
        student.setMajor(rs.getString("major"));
        student.setPassword(rs.getString("password"));
        return student;
    };

    @Override
    public List<Student> findAll() {
        String sql = "SELECT * FROM Students";
        return jdbcTemplate.query(sql, studentRowMapper);
    }

    @Override
    public Optional<Student> findById(String studentId) {
        String sql = "SELECT * FROM Students WHERE student_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, studentRowMapper, studentId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public int save(Student student) {
        String sql = "INSERT INTO Students (student_id, student_name, gender, birth_date, major, password) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                student.getStudentId(),
                student.getStudentName(),
                student.getGender(),
                student.getBirthDate(),
                student.getMajor(),
                student.getPassword());
    }

    @Override
    public int update(Student student) {
        String sql = "UPDATE Students SET student_name = ?, gender = ?, birth_date = ?, major = ?, password = ? WHERE student_id = ?";
        return jdbcTemplate.update(sql,
                student.getStudentName(),
                student.getGender(),
                student.getBirthDate(),
                student.getMajor(),
                student.getPassword(),
                student.getStudentId());
    }

    @Override
    public int deleteById(String studentId) {
        String sql = "DELETE FROM Students WHERE student_id = ?";
        return jdbcTemplate.update(sql, studentId);
    }

    @Override
    public Optional<Student> findByLogin(String studentId, String password) {
        String sql = "SELECT * FROM Students WHERE student_id = ? AND password = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, studentRowMapper, studentId, password));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}