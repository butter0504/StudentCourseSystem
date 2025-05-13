package com.example.studentcoursesystem.repository;

import com.example.studentcoursesystem.model.StudentCourse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentCourseRepositoryImpl implements StudentCourseRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentCourseRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<StudentCourse> studentCourseRowMapper = (rs, rowNum) -> {
        StudentCourse sc = new StudentCourse();
        sc.setId(rs.getInt("id"));
        sc.setStudentId(rs.getString("student_id"));
        sc.setCourseId(rs.getString("course_id"));
        sc.setScore(rs.getDouble("score"));
        return sc;
    };

    @Override
    public List<StudentCourse> findAll() {
        String sql = "SELECT * FROM Student_Course";
        return jdbcTemplate.query(sql, studentCourseRowMapper);
    }

    @Override
    public List<StudentCourse> findByStudentId(String studentId) {
        String sql = "SELECT * FROM Student_Course WHERE student_id = ?";
        return jdbcTemplate.query(sql, studentCourseRowMapper, studentId);
    }

    @Override
    public List<StudentCourse> findByCourseId(String courseId) {
        String sql = "SELECT * FROM Student_Course WHERE course_id = ?";
        return jdbcTemplate.query(sql, studentCourseRowMapper, courseId);
    }

    @Override
    public Optional<StudentCourse> findById(Integer id) {
        String sql = "SELECT * FROM Student_Course WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, studentCourseRowMapper, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public int save(StudentCourse studentCourse) {
        String sql = "INSERT INTO Student_Course (student_id, course_id, score) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql,
                studentCourse.getStudentId(),
                studentCourse.getCourseId(),
                studentCourse.getScore());
    }

    @Override
    public int updateScore(StudentCourse studentCourse) {
        String sql = "UPDATE Student_Course SET score = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                studentCourse.getScore(),
                studentCourse.getId());
    }

    @Override
    public int deleteById(Integer id) {
        String sql = "DELETE FROM Student_Course WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public int deleteByStudentAndCourse(String studentId, String courseId) {
        String sql = "DELETE FROM Student_Course WHERE student_id = ? AND course_id = ?";
        return jdbcTemplate.update(sql, studentId, courseId);
    }

    @Override
    public boolean existsByStudentAndCourse(String studentId, String courseId) {
        String sql = "SELECT COUNT(*) FROM Student_Course WHERE student_id = ? AND course_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, studentId, courseId);
        return count != null && count > 0;
    }
}