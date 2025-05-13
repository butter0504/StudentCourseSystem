package com.example.studentcoursesystem;

import com.example.studentcoursesystem.ui.LoginFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class StudentCourseSystemApplication {

    public static void main(String[] args) {
        // Spring标准启动方式
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(StudentCourseSystemApplication.class)
                .headless(false).run(args);

        // 启动Swing界面
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = ctx.getBean(LoginFrame.class);
            loginFrame.setVisible(true);
        });
        System.out.println(System.getProperty("java.library.path"));
    }
}