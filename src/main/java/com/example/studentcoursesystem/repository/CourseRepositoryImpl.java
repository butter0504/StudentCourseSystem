package com.example.studentcoursesystem.repository;

import com.example.studentcoursesystem.model.Course;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepositoryImpl implements CourseRepository {

    private final JdbcTemplate jdbcTemplate;

    public CourseRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Course> courseRowMapper = (rs, rowNum) -> {
        Course course = new Course();
        course.setCourseId(rs.getString("course_id"));
        course.setCourseName(rs.getString("course_name"));
        course.setCredit(rs.getInt("credit"));
        course.setTeacher(rs.getString("teacher"));
        course.setClassTime(rs.getString("class_time"));
        course.setCapacity(rs.getInt("capacity"));
        return course;
    };

    @Override
    public List<Course> findAll() {
        String sql = "SELECT * FROM Courses";
        return jdbcTemplate.query(sql, courseRowMapper);
    }

    @Override
    public Optional<Course> findById(String courseId) {
        String sql = "SELECT * FROM Courses WHERE course_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, courseRowMapper, courseId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public int save(Course course) {
        String sql = "INSERT INTO Courses (course_id, course_name, credit, teacher, class_time, capacity) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                course.getCourseId(),
                course.getCourseName(),
                course.getCredit(),
                course.getTeacher(),
                course.getClassTime(),
                course.getCapacity());
    }

    @Override
    public int update(Course course) {
        String sql = "UPDATE Courses SET course_name = ?, credit = ?, teacher = ?, class_time = ?, capacity = ? WHERE course_id = ?";
        return jdbcTemplate.update(sql,
                course.getCourseName(),
                course.getCredit(),
                course.getTeacher(),
                course.getClassTime(),
                course.getCapacity(),
                course.getCourseId());
    }

    @Override
    public int deleteById(String courseId) {
        String sql = "DELETE FROM Courses WHERE course_id = ?";
        return jdbcTemplate.update(sql, courseId);
    }

    @Override
    public List<Course> findAvailableCoursesForStudent(String studentId) {
        String sql = "SELECT c.* FROM Courses c " +
                "WHERE c.course_id NOT IN " +
                "(SELECT sc.course_id FROM Student_Course sc WHERE sc.student_id = ?) " +
                "AND (SELECT COUNT(*) FROM Student_Course sc WHERE sc.course_id = c.course_id) < c.capacity";
        return jdbcTemplate.query(sql, courseRowMapper, studentId);
    }
}