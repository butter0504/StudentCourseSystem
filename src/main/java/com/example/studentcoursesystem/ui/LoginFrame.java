package com.example.studentcoursesystem.ui;

import com.example.studentcoursesystem.model.Student;
import com.example.studentcoursesystem.service.CourseService;
import com.example.studentcoursesystem.service.StudentCourseService;
import com.example.studentcoursesystem.service.StudentService;
import jakarta.el.BeanNameResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

@Component
public class LoginFrame extends JFrame {

    private final StudentService studentService;
    private final CourseService courseService;
    private final StudentCourseService studentCourseService;
    private JTextField studentIdField;
    private JPasswordField passwordField;

    @Autowired
    public LoginFrame(StudentService studentService, CourseService courseService, StudentCourseService studentCourseService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.studentCourseService = studentCourseService;
        initUI();
    }

    private void initUI() {
        setTitle("学生选课系统 - 登录");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // 学号标签和文本框
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("学号:"), gbc);

        gbc.gridx = 1;
        studentIdField = new JTextField(15);
        panel.add(studentIdField, gbc);

        // 密码标签和密码框
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("密码:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // 登录按钮
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        panel.add(loginButton, gbc);

        // 回车键登录
        getRootPane().setDefaultButton(loginButton);

        add(panel);
    }

    private void handleLogin() {
        String studentId = studentIdField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (studentId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "学号和密码不能为空!", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Optional<Student> student = studentService.login(studentId, password);
            if (student.isPresent()) {
                JOptionPane.showMessageDialog(this, "登录成功, 欢迎 " + student.get().getStudentName() + "!");
                openMainFrame();
                dispose(); // 关闭登录窗口
            } else {
                JOptionPane.showMessageDialog(this, "学号或密码错误!", "错误", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "登录失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void openMainFrame() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                MainFrame mainFrame = new MainFrame(studentService, courseService, studentCourseService);
                mainFrame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "无法打开主界面: " + e.getMessage(),
                        "错误", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }}