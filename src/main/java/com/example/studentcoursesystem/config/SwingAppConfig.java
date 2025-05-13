package com.example.studentcoursesystem.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;

@Configuration
@ConditionalOnClass(JFrame.class)
public class SwingAppConfig {

    @Bean(name = "swingMainFrame")
    public JFrame mainFrame() {
        return new JFrame();
    }
}