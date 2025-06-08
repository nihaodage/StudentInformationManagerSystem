package app.views;

import app.models.ConnectDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class ScoreAnalysis {

    // 子窗口
    private static JInternalFrame frame = new JInternalFrame("学生考试成绩分析", false, false, false, false);

    // 界面组件
    private static JComboBox<String> courseComboBox = new JComboBox<>();
    private static JTextField passScoreField = new JTextField();
    private static JTextField excellentScoreField = new JTextField();
    private static JButton analyzeButton = new JButton("开始分析");
    private static JButton exitButton = new JButton("退出");
    private static JTextField examNumField = new JTextField();
    private static JTextField avgScoreField = new JTextField();
    private static JTextField passRateField = new JTextField();
    private static JTextField excellentRateField = new JTextField();
    private static DefaultTableModel scoreTableModel;
    private static JTable scoreTable;

    public static void show(JFrame menu) {
        // 创建桌面面板，使得子窗口可以随意拖动
        JDesktopPane jDesktopPane = new JDesktopPane();
        jDesktopPane.setBounds(0, 0, menu.getWidth(), menu.getHeight());
        jDesktopPane.setOpaque(false);
        courseComboBox.removeAllItems();

        // 创建子窗口
        frame = new JInternalFrame("学生考试成绩分析", false, false, false, false);
        frame.setVisible(true);
        frame.setSize(menu.getWidth() / 2 + menu.getWidth() / 4, menu.getHeight() / 2 + menu.getHeight() / 4);
        frame.setLocation(menu.getWidth() / 2 - frame.getWidth() / 2, menu.getHeight() / 2 - frame.getHeight() / 2);

        // 使用BorderLayout作为主布局
        frame.setLayout(new BorderLayout());

        // 创建主面板，使用BoxLayout垂直排列组件
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 初始化表格模型
        String[] scoreColumnNames = {"区间", "人数", "比例"};
        scoreTableModel = new DefaultTableModel(scoreColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        scoreTable = new JTable(scoreTableModel);
        scoreTable.setRowHeight(25);
        scoreTable.getTableHeader().setReorderingAllowed(false);

        // 创建头部面板
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel);

        // 添加第一个分割线
        JSeparator separator1 = new JSeparator(JSeparator.HORIZONTAL);
        separator1.setPreferredSize(new Dimension(menu.getWidth(), 10));
        separator1.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        mainPanel.add(separator1);

        // 创建统计信息面板
        JPanel statsPanel = createStatsPanel();
        mainPanel.add(statsPanel);

        // 添加第二个分割线
        JSeparator separator2 = new JSeparator(JSeparator.HORIZONTAL);
        separator2.setPreferredSize(new Dimension(menu.getWidth(), 10));
        separator2.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        mainPanel.add(separator2);

        // 创建表格面板
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel);

        // 将主面板添加到框架中
        frame.add(mainPanel, BorderLayout.CENTER);

        // 清空并初始化表格数据
        initializeTableData();

        // 将子窗口添加到桌面面板
        jDesktopPane.add(frame);

        // 创建分层面板，使得子窗口在主窗口之上
        JLayeredPane jLayeredPane = menu.getLayeredPane();
        jLayeredPane.add(jDesktopPane, JLayeredPane.PALETTE_LAYER);

        add_listen_event();
        // 重新验证和绘制父窗口
        menu.validate();
        menu.repaint();
    }

    private static JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        panel.add(new JLabel("课程:"));

        courseComboBox.setPreferredSize(new Dimension(120, 25));
        String sql="select subject_name from subject_information";
        ResultSet rs=null;
        try {
            rs = ConnectDB.st.executeQuery(sql);

            while(rs.next())
            {
                String course=rs.getString(1);
                courseComboBox.addItem(course);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        panel.add(courseComboBox);

        panel.add(new JLabel("及格分:"));
        passScoreField.setPreferredSize(new Dimension(60, 25));
        passScoreField.setText("60");
        panel.add(passScoreField);

        panel.add(new JLabel("优秀分:"));
        excellentScoreField.setPreferredSize(new Dimension(60, 25));
        excellentScoreField.setText("90");
        panel.add(excellentScoreField);

        analyzeButton.setPreferredSize(new Dimension(120, 25));
        panel.add(analyzeButton);

        exitButton.setPreferredSize(new Dimension(80, 25));
        panel.add(exitButton);

        return panel;
    }

    private static JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        panel.add(new JLabel("考试人数:"));
        examNumField.setPreferredSize(new Dimension(60, 25));
        examNumField.setText("0");
        examNumField.setEditable(false);
        panel.add(examNumField);

        panel.add(new JLabel("平均分:"));
        avgScoreField.setPreferredSize(new Dimension(60, 25));
        avgScoreField.setText("0");
        avgScoreField.setEditable(false);
        panel.add(avgScoreField);

        panel.add(new JLabel("及格率:"));
        passRateField.setPreferredSize(new Dimension(80, 25));
        passRateField.setText("0.00%");
        passRateField.setEditable(false);
        panel.add(passRateField);

        panel.add(new JLabel("优秀率:"));
        excellentRateField.setPreferredSize(new Dimension(80, 25));
        excellentRateField.setText("0.00%");
        excellentRateField.setEditable(false);
        panel.add(excellentRateField);

        return panel;
    }

    private static JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 添加表格滚动面板
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private static void initializeTableData() {
        // 清空表格
        scoreTableModel.setRowCount(0);

        // 添加初始数据行
        Object[] row1 = {"90及以上", "0", "0.00%"};
        Object[] row2 = {"80-90", "0", "0.00%"};
        Object[] row3 = {"70-80", "0", "0.00%"};
        Object[] row4 = {"60-70", "0", "0.00%"};
        Object[] row5 = {"60及以下", "0", "0.00%"};

        scoreTableModel.addRow(row1);
        scoreTableModel.addRow(row2);
        scoreTableModel.addRow(row3);
        scoreTableModel.addRow(row4);
        scoreTableModel.addRow(row5);
    }


    public static void add_listen_event(){
        ActionListener analysis=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int total=0;//总人数
                int totalScore=0;//总分
                int[] section=new int[10];//从上往下看，0为90及以上
                int pass=0;//及格人数
                int great=0;//优秀人数

                String subject=(String)courseComboBox.getSelectedItem();
                int passScore=Integer.parseInt(passScoreField.getText());
                int greatScore=Integer.parseInt(excellentScoreField.getText());

                ResultSet rs;
                try
                {
                    String sql=String.format("select score from inputscore where subject_name='%s'",subject);
                    rs=ConnectDB.st.executeQuery(sql);

                    while(rs.next())
                    {
                        int score=rs.getInt(1);
                        if(score>greatScore)great++;//要是优秀就优秀人数+1
                        if(score>passScore)pass++;

                        total++;

                        if(score>90)section[0]++;
                        else if(score>80)section[1]++;
                        else if(score>70)section[2]++;
                        else if(score>60)section[3]++;
                        else section[4]++;

                        totalScore+=score;
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }



                if(total!=0) {
                    DecimalFormat df = new DecimalFormat("#.00");
                    double avg = (double) totalScore / total;
                    double passPercent = (double) pass / total * 100;
                    double greatPercent = (double) great / total * 100;

                    examNumField.setText(String.valueOf(total));
                    avgScoreField.setText(df.format(avg));
                    passRateField.setText(String.format("%s%s",df.format(passPercent),'%'));
                    excellentRateField.setText(String.format("%s%s",df.format(greatPercent),'%'));

                    for(int i=0;i<5;i++)
                    {
                        int peopleNum=section[i];
                        scoreTable.setValueAt(String.valueOf(peopleNum),i,1);
                        double percent= (double) peopleNum /total*100;
                        if(percent==0)
                        {
                            scoreTable.setValueAt("0.00%",i,2);
                        }else
                        scoreTable.setValueAt(String.format("%s%s",df.format(percent),'%'),i,2);
                    }
                }else {
                    examNumField.setText("0");
                    avgScoreField.setText("0");
                    passRateField.setText("0");
                    excellentRateField.setText("0");

                    for(int i=0;i<5;i++)
                    {
                        scoreTable.setValueAt("0",i,1);
                        scoreTable.setValueAt("0",i,2);
                    }
                }



            }
        };
        analyzeButton.addActionListener(analysis);

        ActionListener exit=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analyzeButton.removeActionListener(analysis);

                examNumField.setText("0");
                avgScoreField.setText("0");
                passRateField.setText("0");
                excellentRateField.setText("0");

                for(int i=0;i<5;i++)
                {
                    scoreTable.setValueAt("0",i,1);
                    scoreTable.setValueAt("0",i,2);
                }

                passScoreField.setText("60");
                excellentScoreField.setText("90");
                frame.dispose();
            }
        };
        exitButton.addActionListener(exit);
    }
}
