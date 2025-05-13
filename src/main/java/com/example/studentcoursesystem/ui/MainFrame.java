package com.example.studentcoursesystem.ui;

import com.example.studentcoursesystem.model.Course;
import com.example.studentcoursesystem.model.Student;
import com.example.studentcoursesystem.model.StudentCourse;
import com.example.studentcoursesystem.service.CourseService;
import com.example.studentcoursesystem.service.StudentCourseService;
import com.example.studentcoursesystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Component
public class MainFrame extends JFrame {

    private final StudentService studentService;
    private final CourseService courseService;
    private final StudentCourseService studentCourseService;

    private JTabbedPane tabbedPane;
    private JTable studentTable;
    private JTable courseTable;
    private JTable studentCourseTable;

    @Autowired
    public MainFrame(StudentService studentService, CourseService courseService,
                     StudentCourseService studentCourseService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.studentCourseService = studentCourseService;

        initUI();
        loadStudents();
        loadCourses();
        loadStudentCourses();
    }

    private void initUI() {
        setTitle("学生选课系统 - 管理界面");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建菜单栏
        JMenuBar menuBar = new JMenuBar();

        // 系统菜单
        JMenu systemMenu = new JMenu("系统");
        JMenuItem exitItem = new JMenuItem("退出");
        exitItem.addActionListener(e -> System.exit(0));
        systemMenu.add(exitItem);
        menuBar.add(systemMenu);

        // 帮助菜单
        JMenu helpMenu = new JMenu("帮助");
        JMenuItem aboutItem = new JMenuItem("关于");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        // 创建选项卡面板
        tabbedPane = new JTabbedPane();

        // 学生管理面板
        JPanel studentPanel = createStudentPanel();
        tabbedPane.addTab("学生管理", studentPanel);

        // 课程管理面板
        JPanel coursePanel = createCoursePanel();
        tabbedPane.addTab("课程管理", coursePanel);

        // 选课管理面板
        JPanel scPanel = createStudentCoursePanel();
        tabbedPane.addTab("选课管理", scPanel);

        add(tabbedPane);
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 学生表格
        studentTable = new JTable();
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("添加学生");
        addButton.addActionListener(e -> showAddStudentDialog());

        JButton editButton = new JButton("编辑学生");
        editButton.addActionListener(e -> showEditStudentDialog());

        JButton deleteButton = new JButton("删除学生");
        deleteButton.addActionListener(e -> deleteSelectedStudent());

        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> loadStudents());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCoursePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 课程表格
        courseTable = new JTable();
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(courseTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("添加课程");
        addButton.addActionListener(e -> showAddCourseDialog());

        JButton editButton = new JButton("编辑课程");
        editButton.addActionListener(e -> showEditCourseDialog());

        JButton deleteButton = new JButton("删除课程");
        deleteButton.addActionListener(e -> deleteSelectedCourse());

        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> loadCourses());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStudentCoursePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 选课表格
        studentCourseTable = new JTable();
        studentCourseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(studentCourseTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton selectButton = new JButton("选课");
        selectButton.addActionListener(e -> showSelectCourseDialog());

        JButton dropButton = new JButton("退课");
        dropButton.addActionListener(e -> dropSelectedCourse());

        JButton scoreButton = new JButton("录入成绩");
        scoreButton.addActionListener(e -> inputScoreDialog());

        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> loadStudentCourses());

        buttonPanel.add(selectButton);
        buttonPanel.add(dropButton);
        buttonPanel.add(scoreButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            model.setColumnIdentifiers(new String[]{"学号", "姓名", "性别", "出生日期", "专业"});

            for (Student student : students) {
                model.addRow(new Object[]{
                        student.getStudentId(),
                        student.getStudentName(),
                        student.getGender(),
                        student.getBirthDate(),
                        student.getMajor()
                });
            }

            studentTable.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载学生数据失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            model.setColumnIdentifiers(new String[]{"课程号", "课程名称", "学分", "教师", "上课时间", "容量"});

            for (Course course : courses) {
                model.addRow(new Object[]{
                        course.getCourseId(),
                        course.getCourseName(),
                        course.getCredit(),
                        course.getTeacher(),
                        course.getClassTime(),
                        course.getCapacity()
                });
            }

            courseTable.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载课程数据失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadStudentCourses() {
        try {
            List<StudentCourse> studentCourses = studentCourseService.getAllStudentCourses();
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            model.setColumnIdentifiers(new String[]{"ID", "学号", "课程号", "成绩"});

            for (StudentCourse sc : studentCourses) {
                model.addRow(new Object[]{
                        sc.getId(),
                        sc.getStudentId(),
                        sc.getCourseId(),
                        sc.getScore() == null ? "未评分" : String.format("%.1f", sc.getScore())
                });
            }

            studentCourseTable.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载选课数据失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ========== 学生管理相关方法 ==========
    private void showAddStudentDialog() {
        JDialog dialog = new JDialog(this, "添加学生", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"男", "女"});
        JTextField birthField = new JTextField();
        JTextField majorField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("学号:"));
        panel.add(idField);
        panel.add(new JLabel("姓名:"));
        panel.add(nameField);
        panel.add(new JLabel("性别:"));
        panel.add(genderCombo);
        panel.add(new JLabel("出生日期(YYYY-MM-DD):"));
        panel.add(birthField);
        panel.add(new JLabel("专业:"));
        panel.add(majorField);
        panel.add(new JLabel("密码:"));
        panel.add(passwordField);

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            try {
                Student student = new Student();
                student.setStudentId(idField.getText().trim());
                student.setStudentName(nameField.getText().trim());
                student.setGender(genderCombo.getSelectedItem().toString());
                student.setBirthDate(Date.valueOf(birthField.getText().trim()).toLocalDate());
                student.setMajor(majorField.getText().trim());
                student.setPassword(new String(passwordField.getPassword()).trim());

                if (studentService.addStudent(student)) {
                    JOptionPane.showMessageDialog(dialog, "添加学生成功!");
                    dialog.dispose();
                    loadStudents();
                } else {
                    JOptionPane.showMessageDialog(dialog, "添加学生失败!", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditStudentDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要编辑的学生!", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = studentTable.getValueAt(selectedRow, 0).toString();
        Optional<Student> studentOpt = studentService.getStudentById(studentId);

        if (!studentOpt.isPresent()) {
            JOptionPane.showMessageDialog(this, "找不到选中的学生!", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = studentOpt.get();

        JDialog dialog = new JDialog(this, "编辑学生", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField idField = new JTextField(student.getStudentId());
        idField.setEditable(false);
        JTextField nameField = new JTextField(student.getStudentName());
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"男", "女"});
        genderCombo.setSelectedItem(student.getGender());
        JTextField birthField = new JTextField(student.getBirthDate().toString());
        JTextField majorField = new JTextField(student.getMajor());
        JPasswordField passwordField = new JPasswordField(student.getPassword());

        panel.add(new JLabel("学号:"));
        panel.add(idField);
        panel.add(new JLabel("姓名:"));
        panel.add(nameField);
        panel.add(new JLabel("性别:"));
        panel.add(genderCombo);
        panel.add(new JLabel("出生日期(YYYY-MM-DD):"));
        panel.add(birthField);
        panel.add(new JLabel("专业:"));
        panel.add(majorField);
        panel.add(new JLabel("密码:"));
        panel.add(passwordField);

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            try {
                student.setStudentName(nameField.getText().trim());
                student.setGender(genderCombo.getSelectedItem().toString());
                student.setBirthDate(Date.valueOf(birthField.getText().trim()).toLocalDate());
                student.setMajor(majorField.getText().trim());
                student.setPassword(new String(passwordField.getPassword()).trim());

                if (studentService.updateStudent(student)) {
                    JOptionPane.showMessageDialog(dialog, "更新学生信息成功!");
                    dialog.dispose();
                    loadStudents();
                } else {
                    JOptionPane.showMessageDialog(dialog, "更新学生信息失败!", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的学生!", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = studentTable.getValueAt(selectedRow, 0).toString();
        String studentName = studentTable.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要删除学生 " + studentName + " (" + studentId + ") 吗?",
                "确认删除", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (studentService.deleteStudent(studentId)) {
                    JOptionPane.showMessageDialog(this, "删除学生成功!");
                    loadStudents();
                } else {
                    JOptionPane.showMessageDialog(this, "删除学生失败!", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // ========== 课程管理相关方法 ==========
    private void showAddCourseDialog() {
        JDialog dialog = new JDialog(this, "添加课程", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JSpinner creditSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        JTextField teacherField = new JTextField();
        JTextField timeField = new JTextField();
        JSpinner capacitySpinner = new JSpinner(new SpinnerNumberModel(30, 1, 200, 1));

        panel.add(new JLabel("课程号:"));
        panel.add(idField);
        panel.add(new JLabel("课程名称:"));
        panel.add(nameField);
        panel.add(new JLabel("学分:"));
        panel.add(creditSpinner);
        panel.add(new JLabel("授课教师:"));
        panel.add(teacherField);
        panel.add(new JLabel("上课时间:"));
        panel.add(timeField);
        panel.add(new JLabel("容量:"));
        panel.add(capacitySpinner);

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            try {
                Course course = new Course();
                course.setCourseId(idField.getText().trim());
                course.setCourseName(nameField.getText().trim());
                course.setCredit((Integer) creditSpinner.getValue());
                course.setTeacher(teacherField.getText().trim());
                course.setClassTime(timeField.getText().trim());
                course.setCapacity((Integer) capacitySpinner.getValue());

                if (courseService.addCourse(course)) {
                    JOptionPane.showMessageDialog(dialog, "添加课程成功!");
                    dialog.dispose();
                    loadCourses();
                } else {
                    JOptionPane.showMessageDialog(dialog, "添加课程失败!", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditCourseDialog() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要编辑的课程!", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = courseTable.getValueAt(selectedRow, 0).toString();
        Optional<Course> courseOpt = courseService.getCourseById(courseId);

        if (!courseOpt.isPresent()) {
            JOptionPane.showMessageDialog(this, "找不到选中的课程!", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Course course = courseOpt.get();

        JDialog dialog = new JDialog(this, "编辑课程", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField idField = new JTextField(course.getCourseId());
        idField.setEditable(false);
        JTextField nameField = new JTextField(course.getCourseName());
        JSpinner creditSpinner = new JSpinner(new SpinnerNumberModel(
                Integer.valueOf(course.getCredit()), // 显式转换为 Integer
                Integer.valueOf(1),                  // 最小值
                Integer.valueOf(10),                 // 最大值
                Integer.valueOf(1)                   // 步长
        ));
        JTextField teacherField = new JTextField(course.getTeacher());
        JTextField timeField = new JTextField(course.getClassTime());
        JSpinner capacitySpinner = new JSpinner(new SpinnerNumberModel(
                (int)course.getCapacity(), 1, 200, 1));

        panel.add(new JLabel("课程号:"));
        panel.add(idField);
        panel.add(new JLabel("课程名称:"));
        panel.add(nameField);
        panel.add(new JLabel("学分:"));
        panel.add(creditSpinner);
        panel.add(new JLabel("授课教师:"));
        panel.add(teacherField);
        panel.add(new JLabel("上课时间:"));
        panel.add(timeField);
        panel.add(new JLabel("容量:"));
        panel.add(capacitySpinner);

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            try {
                course.setCourseName(nameField.getText().trim());
                course.setCredit((Integer) creditSpinner.getValue());
                course.setTeacher(teacherField.getText().trim());
                course.setClassTime(timeField.getText().trim());
                course.setCapacity((Integer) capacitySpinner.getValue());

                if (courseService.updateCourse(course)) {
                    JOptionPane.showMessageDialog(dialog, "更新课程信息成功!");
                    dialog.dispose();
                    loadCourses();
                } else {
                    JOptionPane.showMessageDialog(dialog, "更新课程信息失败!", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的课程!", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = courseTable.getValueAt(selectedRow, 0).toString();
        String courseName = courseTable.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要删除课程 " + courseName + " (" + courseId + ") 吗?\n这将同时删除所有相关的选课记录!",
                "确认删除", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (courseService.deleteCourse(courseId)) {
                    JOptionPane.showMessageDialog(this, "删除课程成功!");
                    loadCourses();
                    loadStudentCourses();
                } else {
                    JOptionPane.showMessageDialog(this, "删除课程失败!", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // ========== 选课管理相关方法 ==========
    private void showSelectCourseDialog() {
        JDialog dialog = new JDialog(this, "学生选课", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());

        // 学生选择
        JPanel studentPanel = new JPanel();
        studentPanel.add(new JLabel("学生学号:"));
        JTextField studentIdField = new JTextField(15);
        studentPanel.add(studentIdField);

        JButton searchButton = new JButton("查询可选课程");
        studentPanel.add(searchButton);

        panel.add(studentPanel, BorderLayout.NORTH);

        // 课程表格
        JTable availableCoursesTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(availableCoursesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        JButton selectButton = new JButton("选课");
        JButton cancelButton = new JButton("取消");
        buttonPanel.add(selectButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> {
            String studentId = studentIdField.getText().trim();
            if (studentId.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "请输入学生学号!", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                List<Course> availableCourses = courseService.getAvailableCoursesForStudent(studentId);
                DefaultTableModel model = new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                model.setColumnIdentifiers(new String[]{"课程号", "课程名称", "学分", "教师", "上课时间", "剩余名额"});

                for (Course course : availableCourses) {
                    int enrolled = studentCourseService.getStudentsByCourseId(course.getCourseId()).size();
                    int remaining = course.getCapacity() - enrolled;
                    model.addRow(new Object[]{
                            course.getCourseId(),
                            course.getCourseName(),
                            course.getCredit(),
                            course.getTeacher(),
                            course.getClassTime(),
                            remaining
                    });
                }

                availableCoursesTable.setModel(model);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "查询失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        selectButton.addActionListener(e -> {
            int selectedRow = availableCoursesTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "请先选择要选的课程!", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String studentId = studentIdField.getText().trim();
            String courseId = availableCoursesTable.getValueAt(selectedRow, 0).toString();

            try {
                StudentCourse sc = new StudentCourse();
                sc.setStudentId(studentId);
                sc.setCourseId(courseId);

                if (studentCourseService.selectCourse(sc)) {
                    JOptionPane.showMessageDialog(dialog, "选课成功!");
                    dialog.dispose();
                    loadStudentCourses();
                } else {
                    JOptionPane.showMessageDialog(dialog, "选课失败! 可能已经选过该课程", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "选课失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void dropSelectedCourse() {
        int selectedRow = studentCourseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要退的课程!", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) studentCourseTable.getValueAt(selectedRow, 0);
        String studentId = studentCourseTable.getValueAt(selectedRow, 1).toString();
        String courseId = studentCourseTable.getValueAt(selectedRow, 2).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要退选课程 " + courseId + " 吗?",
                "确认退课", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (studentCourseService.dropCourse(id)) {
                    JOptionPane.showMessageDialog(this, "退课成功!");
                    loadStudentCourses();
                } else {
                    JOptionPane.showMessageDialog(this, "退课失败!", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void inputScoreDialog() {
        int selectedRow = studentCourseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要录入成绩的记录!", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) studentCourseTable.getValueAt(selectedRow, 0);
        String studentId = studentCourseTable.getValueAt(selectedRow, 1).toString();
        String courseId = studentCourseTable.getValueAt(selectedRow, 2).toString();
        Object scoreObj = studentCourseTable.getValueAt(selectedRow, 3);

        JDialog dialog = new JDialog(this, "录入成绩", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("学生学号: " + studentId));
        panel.add(new JLabel("课程号: " + courseId));

        JPanel scorePanel = new JPanel();
        scorePanel.add(new JLabel("成绩:"));
        JTextField scoreField = new JTextField(5);
        if (!"未评分".equals(scoreObj.toString())) {
            scoreField.setText(scoreObj.toString());
        }
        scorePanel.add(scoreField);
        panel.add(scorePanel);

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            try {
                double score = Double.parseDouble(scoreField.getText().trim());
                if (score < 0 || score > 100) {
                    JOptionPane.showMessageDialog(dialog, "成绩必须在0-100之间!", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                StudentCourse sc = new StudentCourse();
                sc.setId(id);
                sc.setScore(score);

                if (studentCourseService.updateScore(sc)) {
                    JOptionPane.showMessageDialog(dialog, "成绩录入成功!");
                    dialog.dispose();
                    loadStudentCourses();
                } else {
                    JOptionPane.showMessageDialog(dialog, "成绩录入失败!", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "请输入有效的数字成绩!", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "学生选课系统\n版本: 1.0\n开发人员: [你的名字]",
                "关于", JOptionPane.INFORMATION_MESSAGE);
    }
}