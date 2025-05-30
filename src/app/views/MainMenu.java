package app.views;

import javax.management.remote.JMXServiceURL;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MainMenu {

    //菜单栏外部
    JMenuBar topmenu=new JMenuBar();
    JMenu person=new JMenu("人事管理");
    JMenu subject=new JMenu("科目管理");
    JMenu exam=new JMenu("考试管理");
    JMenu system=new JMenu("系统管理");

    //菜单栏内部
    //人事管理
    static JMenuItem studentinformation =new JMenuItem("学生信息");
    static JMenuItem techerinformation=new JMenuItem("教师信息");
    //科目管理
    static JMenuItem subjectinformation=new JMenuItem("科目信息");
    static JMenuItem give_lesson=new JMenuItem("授课资格");
    //考试管理
    static JMenuItem input_data =new JMenuItem("录入成绩");
    static JMenuItem score_analyse =new JMenuItem("成绩分析");
    //系统管理
    static JMenuItem user_maintain=new JMenuItem("用户维护");
    static JMenuItem system_exit=new JMenuItem("系统退出");

    static JFrame menu =new JFrame("学生成绩管理系统");//menu为进来的界面
    public void CreateMenu()
    {

        menu.setBounds(Menu.width/4,Menu.height/4,792,600);//设置窗体大小
        menu.setVisible(true);//使窗体可见
        menu.setLayout(new GridBagLayout());//使窗体是Grid布局模式
        menu.setResizable(false);//使窗体不可改变大小
        menu.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gri=new GridBagConstraints();//定义grid container

        //窗体中的容器
        JLabel img=new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/app/image/main.jpg"))));


        //设置组件的样式
        gri.insets=new Insets(0,5,0,5);
        gri.fill=GridBagConstraints.HORIZONTAL;



        person.add(studentinformation);
        person.add(techerinformation);

        subject.add(subjectinformation);
        subject.add(give_lesson);

        exam.add(input_data);
        exam.add(score_analyse);

        system.add(user_maintain);
        system.add(system_exit);


        //添加人事管理控件
        gri.gridx=0;
        gri.gridy=0;
        topmenu.add(person,gri);

        //添加学科管理控件
        gri.gridx=1;
        gri.gridy=0;
        topmenu.add(subject,gri);

        //添加学科管理控件
        gri.gridx=2;
        gri.gridy=0;
        topmenu.add(exam,gri);

        //添加系统管理控件
        gri.gridx=3;
        gri.gridy=0;
        topmenu.add(system,gri);

        //将菜单栏添加到显示界面中
        menu.add(topmenu);

        //添加图片
        gri.gridx=0;
        gri.gridy=1;
        gri.gridwidth=8;
        menu.add(img,gri);

        menu.repaint();
        menu.pack();

        start();
    }

    public static void start()
    {
        studentinformation.addActionListener(new ActionListener() {//点击学生信息
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentInformation.show(menu);
                System.out.println("点击学生信息");
            }
        });

        techerinformation.addActionListener(new ActionListener() {//点击教师信息
            @Override
            public void actionPerformed(ActionEvent e) {
                TeacherInformation.show(menu);
                System.out.println("点击教师信息");
            }
        });

        subjectinformation.addActionListener(new ActionListener() {//点击科目信息
            @Override
            public void actionPerformed(ActionEvent e) {
                SubjectInformation.show(menu);
                System.out.println("点击科目信息");
            }
        });

        give_lesson.addActionListener(new ActionListener() {//点击授课资格
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("点击授课资格");
            }
        });

        input_data.addActionListener(new ActionListener() {//点击录入成绩
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("点击录入成绩");
            }
        });

        score_analyse.addActionListener(new ActionListener() {//点击成绩分析
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("点击成绩分析");
            }
        });

        user_maintain.addActionListener(new ActionListener() {//点击用户维护
            @Override
            public void actionPerformed(ActionEvent e) {
                UserMaintain.show(menu);
                System.out.println("点击用户维护");
            }
        });

        system_exit.addActionListener(new ActionListener() {//点击系统退出
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("点击系统退出");
            }
        });

    }

}
