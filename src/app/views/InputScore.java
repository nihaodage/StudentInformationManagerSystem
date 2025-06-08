package app.views;

import app.models.ConnectDB;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.Objects;

public class InputScore {

    //子窗口
    static JInternalFrame frame=new JInternalFrame("学生考试成绩录入", false, false, false, false);

    static JTextField score_t=new JTextField();
    static JTextField time_t=new JTextField();
    static JComboBox<String> subject_t=new JComboBox<>();

    static JButton addData=new JButton("添加");
    static JButton deleteData=new JButton("删除");
    static JButton exitData=new JButton("退出");
    static JButton alterData=new JButton("修改");
    static JButton saveData=new JButton("导出");

    static String[] columName={"学生编码","学生姓名","年龄","性别","联系电话","家庭地址"};//列名
    static DefaultTableModel tableModel=new DefaultTableModel(columName,0){
        @Override
        public boolean isCellEditable(int row, int column) {//使单元格不能被编辑
            return false;
        }
    };
    static JTable table =new JTable(tableModel);//创建表格

    // 新增表格相关变量
    static String[] scoreColumnName={"科目编号","考试科目","考试成绩","考试时间"};
    static DefaultTableModel scoreTableModel=new DefaultTableModel(scoreColumnName,0){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    static JTable scoreTable =new JTable(scoreTableModel);

    public static void show(JFrame menu) throws SQLException {
        frame.getContentPane().removeAll();
        tableModel.setRowCount(0);//移除所有行
        subject_t.removeAllItems();//移除所有选项
        scoreTableModel.setRowCount(0); // 清空成绩表格

        //下面的代码负责生成子窗口
        JLayeredPane jLayeredPane=menu.getLayeredPane();//创建分层模版，使得子窗口在主窗口之上

        JDesktopPane jDesktopPane=new JDesktopPane();//创建桌面模版，使得子窗口可以随意拖动
        jDesktopPane.setBounds(0,0,menu.getWidth(),menu.getHeight());//设置桌面模版大小
        jDesktopPane.setOpaque(false);//使得桌面模版背景为透明

        //创建子窗口
        frame.setVisible(true);//使得可见
        frame.setSize(menu.getWidth()/2+menu.getWidth()/4,menu.getHeight()/2+menu.getHeight()/4);//大小
        frame.setLocation(menu.getWidth()/2-frame.getWidth()/2,menu.getHeight()/2-frame.getHeight()/2);

        jDesktopPane.add(frame);//在桌面模板里面添加子窗口
        jLayeredPane.add(jDesktopPane,JLayeredPane.PALETTE_LAYER);//在分层模板里面添加桌面模板为顶层

        // 使用BorderLayout作为主布局
        frame.setLayout(new BorderLayout());

        // 创建主面板，使用BoxLayout垂直排列组件
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 填充学生信息表格
        ResultSet re = null;
        re = ConnectDB.Select("select * from student_information");
        if(re != null) {
            try {
                while(re.next()) {
                    String code = re.getString("student_id");
                    String name = re.getString("student_name");
                    String age = re.getString("age");
                    String gender = re.getString("gender");
                    String telephone = re.getString("telephone");
                    String address = re.getString("address");

                    Object[] rowData = {
                            code, name, age,
                            gender, telephone, address
                    };
                    tableModel.addRow(rowData);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "数据库数据有误",
                        "错误",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }

        // 设置表格样式
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);

        scoreTable.setRowHeight(25);
        scoreTable.getTableHeader().setReorderingAllowed(false);

        // 创建学生信息面板 - 固定显示5行，无边框标题
        JPanel studentPanel = new JPanel(new BorderLayout());
        studentPanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

        // 设置表格固定显示5行
        table.setPreferredScrollableViewportSize(new Dimension(
                0,
                table.getRowHeight() * 5 + table.getTableHeader().getPreferredSize().height
        ));

        JScrollPane studentScrollPane = new JScrollPane(table);
        studentPanel.add(studentScrollPane, BorderLayout.CENTER);

        mainPanel.add(studentPanel);

        // 添加分割线
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        mainPanel.add(separator);

        // 创建成绩信息面板 - 固定显示5行，无边框标题
        JPanel scorePanel = new JPanel(new BorderLayout());
        scorePanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

        // 设置表格固定显示5行
        scoreTable.setPreferredScrollableViewportSize(new Dimension(
                0,
                scoreTable.getRowHeight() * 5 + scoreTable.getTableHeader().getPreferredSize().height
        ));

        JScrollPane scoreScrollPane = new JScrollPane(scoreTable);
        scorePanel.add(scoreScrollPane, BorderLayout.CENTER);

        mainPanel.add(scorePanel);

        // 创建输入面板
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel subjectLabel = new JLabel("科目:");
        inputPanel.add(subjectLabel);
        subject_t.setPreferredSize(new Dimension(120, 25));
        inputPanel.add(subject_t);

        JLabel scoreLabelInput = new JLabel("分数:");
        inputPanel.add(scoreLabelInput);
        score_t.setPreferredSize(new Dimension(80, 25));
        inputPanel.add(score_t);

        JLabel timeLabel = new JLabel("时间:");
        inputPanel.add(timeLabel);
        time_t.setPreferredSize(new Dimension(120, 25));
        inputPanel.add(time_t);

        mainPanel.add(inputPanel);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 设置按钮样式
        Font buttonFont = new Font("宋体", Font.PLAIN, 12);
        addData.setFont(buttonFont);
        deleteData.setFont(buttonFont);
        alterData.setFont(buttonFont);
        exitData.setFont(buttonFont);
        saveData.setFont(buttonFont);

        buttonPanel.add(addData);
        buttonPanel.add(deleteData);
        buttonPanel.add(alterData);
        buttonPanel.add(saveData);
        buttonPanel.add(exitData);

        mainPanel.add(buttonPanel);

        // 将主面板添加到框架中，不使用滚动面板
        frame.add(mainPanel, BorderLayout.CENTER);

        // 找到所有科目加入到下拉框中
        String sql = "select subject_name from subject_information";
        re = ConnectDB.st.executeQuery(sql);
        while(re.next()) {
            String name = re.getString(1);
            subject_t.addItem(name);
        }

        menu.repaint();
        menu.revalidate();

        start();
    }

    // 更新成绩表格的方法
    public static void updateScoreTable(String studentId) {
        scoreTableModel.setRowCount(0); // 清空表格

        try {
            String sql = "SELECT student_id, subject_name, score, time from inputscore where student_id="+studentId;
            ResultSet rs=ConnectDB.st.executeQuery(sql);

            if (rs != null) {
                while (rs.next()) {
                    Object[] row = {
                            rs.getString("student_id"),
                            rs.getString("subject_name"),
                            rs.getString("score"),
                            rs.getString("time")
                    };
                    scoreTableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    frame,
                    "获取成绩数据失败: " + e.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void start()
    {
        // 监听学生表格的选择事件，更新成绩表格
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        String studentId = (String) table.getValueAt(selectedRow, 0);
                        updateScoreTable(studentId);
                    }
                }
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index=table.getSelectedRow();
                if(index==-1)return ;
                String student= (String) table.getValueAt(index,0);
                updateScoreTable(student);
            }
        });

        ActionListener add =new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = table.getSelectedRow();
                if (index < 0) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "请先选择一个学生",
                            "提示",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    return;
                }
                String student= (String) table.getValueAt(index,0);
                String score = score_t.getText();
                String time = time_t.getText();
                String subject = (String) subject_t.getSelectedItem();

                if (subject == null || subject.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "请选择科目",
                            "提示",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    return;
                }

                if (score.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "请输入分数",
                            "提示",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    return;
                }

                if (time.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "请输入考试时间",
                            "提示",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    return;
                }

                String sql = String.format("INSERT into inputscore  VALUES (%s,'%s','%s','%s')", student,subject,score,time);

                //如果sql执行出错，就提出警示框
                SQLException t = ConnectDB.AddData(sql);
                System.out.println(t);
                if (t != null) {
                    String error_message = "";
                    if (t instanceof SQLSyntaxErrorException) {
                        error_message = "学生编码不能为空";
                    } else if (t instanceof SQLIntegrityConstraintViolationException) {
                        error_message = "该学生的该科目成绩已存在";
                    } else {
                        error_message = "添加成绩失败: " + t.getMessage();
                    }

                    JOptionPane.showMessageDialog(
                            frame,
                            error_message,
                            "错误",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    // 添加成功后刷新成绩表格
                    updateScoreTable(student);
                    JOptionPane.showMessageDialog(
                            frame,
                            "成绩添加成功",
                            "成功",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

                clear_textfiled();
            }
        };
        addData.addActionListener(add);//给添加按钮加上事件监听

        ActionListener delete = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int scoreIndex = scoreTable.getSelectedRow();
                if (scoreIndex == -1) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "请先选择要删除的成绩记录！",
                            "错误",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                int studentIndex = table.getSelectedRow();
                if (studentIndex == -1) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "请先选择学生！",
                            "错误",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                String studentId = (String) table.getValueAt(studentIndex, 0);
                String subjectName = (String) scoreTable.getValueAt(scoreIndex, 1);

                int confirm = JOptionPane.showConfirmDialog(
                        frame,
                        "确定要删除该成绩记录吗？",
                        "确认删除",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    String sql = "DELETE FROM inputscore WHERE student_id = " + studentId +
                            " AND subject_name = '" + subjectName + "'";
                    ConnectDB.Delete(sql);
                    updateScoreTable(studentId);
                    JOptionPane.showMessageDialog(
                            frame,
                            "成绩记录已删除",
                            "成功",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        };
        deleteData.addActionListener(delete);

        ActionListener exit = new ActionListener() {//如果点击关闭按钮，就释放资源
            @Override
            public void actionPerformed(ActionEvent e) {
                addData.removeActionListener(add);//移除监听器
                deleteData.removeActionListener(delete);
                clear_textfiled();//清除文本框
                frame.dispose();//关闭窗口
            }
        };
        exitData.addActionListener(exit);//给关闭按钮加上事件监听

        // 修改按钮事件
        ActionListener alter = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int scoreIndex = scoreTable.getSelectedRow();
                if (scoreIndex == -1) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "请先选择要修改的成绩记录！",
                            "错误",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                int studentIndex = table.getSelectedRow();
                if (studentIndex == -1) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "请先选择学生！",
                            "错误",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                String studentId = (String) table.getValueAt(studentIndex, 0);
                String subjectName = (String) scoreTable.getValueAt(scoreIndex, 1);
                String currentScore = (String) scoreTable.getValueAt(scoreIndex, 2);
                String currentTime = (String) scoreTable.getValueAt(scoreIndex, 3);

                // 创建修改对话框
                JDialog dialog = new JDialog((Frame) null, "修改成绩", true);
                dialog.setSize(300, 200);
                dialog.setLocationRelativeTo(frame);
                dialog.setLayout(new GridLayout(4, 2, 10, 10));
                dialog.setResizable(false);

                dialog.add(new JLabel("学生ID:"));
                dialog.add(new JLabel(studentId));

                dialog.add(new JLabel("科目:"));
                dialog.add(new JLabel(subjectName));

                JTextField scoreField = new JTextField(currentScore);
                dialog.add(new JLabel("分数:"));
                dialog.add(scoreField);

                JTextField timeField = new JTextField(currentTime);
                dialog.add(new JLabel("时间:"));
                dialog.add(timeField);

                JPanel buttonPanel = new JPanel();
                JButton saveButton = new JButton("保存");
                JButton cancelButton = new JButton("取消");

                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String newScore = scoreField.getText();
                        String newTime = timeField.getText();

                        if (newScore.isEmpty() || newTime.isEmpty()) {
                            JOptionPane.showMessageDialog(
                                    dialog,
                                    "分数和时间不能为空",
                                    "错误",
                                    JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }

                        String sql = "UPDATE inputscore SET score = '" + newScore +
                                "', time = '" + newTime +
                                "' WHERE student_id = " + studentId +
                                " AND subject_name = '" + subjectName + "'";

                        try {
                            ConnectDB.st.executeUpdate(sql);
                            updateScoreTable(studentId);
                            dialog.dispose();
                            JOptionPane.showMessageDialog(
                                    frame,
                                    "成绩更新成功",
                                    "成功",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(
                                    dialog,
                                    "更新失败: " + ex.getMessage(),
                                    "错误",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                });

                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                });

                buttonPanel.add(saveButton);
                buttonPanel.add(cancelButton);

                dialog.add(new JLabel()); // 占位
                dialog.add(buttonPanel);

                dialog.setVisible(true);
            }
        };
        alterData.addActionListener(alter);

        // 导出按钮事件
        ActionListener save = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int studentIndex = table.getSelectedRow();
                if (studentIndex == -1) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "请先选择学生！",
                            "提示",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    return;
                }

                String studentId = (String) table.getValueAt(studentIndex, 0);
                String studentName = (String) table.getValueAt(studentIndex, 1);

                try {
                    // 创建csv文件
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("选择保存位置");
                    fileChooser.setSelectedFile(new File(studentName+"information.csv"));
                    int result = fileChooser.showSaveDialog(frame);

                    StringBuilder content = new StringBuilder();
                    // 写入表头
                    content.append("科目编号,考试科目,考试成绩,考试时间\n");

                    // 写入数据
                    for (int i = 0; i < scoreTableModel.getRowCount(); i++) {
                        for (int j = 0; j < scoreTableModel.getColumnCount(); j++) {
                            content.append(scoreTableModel.getValueAt(i, j).toString());
                            content.append(",");
                        }
                        content.append("\n");
                    }

                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        try (FileWriter writer = new FileWriter(selectedFile)) {
                            // 写入文件内容（示例内容）
                            writer.write(String.valueOf(content));
                        } catch (IOException ex) {
                            System.out.println(content);
                            JOptionPane.showMessageDialog(frame, "保存失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    JOptionPane.showMessageDialog(
                            frame,
                            "成绩已导出",
                            "成功",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            frame,
                            "导出失败: " + ex.getMessage(),
                            "错误",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };
        saveData.addActionListener(save);
    }

    public static void clear_textfiled()//清除文本框
    {
        score_t.setText("");
        time_t.setText("");
        if (subject_t.getItemCount() > 0) {
            subject_t.setSelectedIndex(0);
        }

        table.getSelectionModel().clearSelection();
        scoreTable.getSelectionModel().clearSelection();
    }
}