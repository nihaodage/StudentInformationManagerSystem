package app.views;

import app.models.ConnectDB;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.*;

public class Login extends Menu {
    public static int frameWidth,frameHeight;

    JTextField account_v=new JTextField();//账号框
    JPasswordField password_v=new JPasswordField();//密码框
    JButton submit_button=new JButton("登录");//提交按钮

    JLabel tip=new JLabel("");

    JFrame frame;
    public void ShowLogin(Container container,JFrame _frame,int _frameWidth,int _frameHeight){
        //获取窗体的分辨率
        frameWidth=_frameWidth;
        frameHeight=_frameHeight;
        frame=_frame;
        JPanel background=new JPanel(new GridBagLayout()) {//创建背景面板
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);

                Graphics g2d=(Graphics2D) g;

                g.setColor(new Color(104, 102, 102, 128));//设置填充颜色
                int rectWidth=frameWidth-frameWidth/4;//矩形的宽度
                int rectHeight=frameHeight-frameHeight/4;//矩形的高度
                int rectx=frameWidth/8;//矩形左上角的x
                int recty=frameHeight/16;//矩形左上角的y

                g.fillRect(rectx,recty,rectWidth,rectHeight);//开始填充，并设置填充位置
            }
        };
        //将背景图插入到窗体中
        container.add(background);
        background.setPreferredSize(new Dimension(frameWidth,frameHeight));//设置background最大尺寸
        background.setOpaque(false);//隐藏提示大小

        //下面是background里面的内容
        JLabel name=new JLabel("学生成绩管理系统");
        JLabel title=new JLabel("登录");
        JLabel account=new JLabel("用户名");
        JLabel password=new JLabel("密码");
        JTextField acc_text=new JTextField();
        JPasswordField pas_text=new JPasswordField();

        JButton submit=new JButton("登录");

        //将账号，密码，提交按钮拿出来
        account_v=acc_text;
        password_v=pas_text;
        submit_button=submit;

        //控件的样式
        acc_text.setPreferredSize(new Dimension(200,30));
        pas_text.setPreferredSize(new Dimension(200,30));
        title.setFont(new Font("黑体",Font.BOLD,30));
        name.setFont(new Font("黑体",Font.BOLD,30));
        tip.setForeground(Color.RED);

        GridBagConstraints gri=new GridBagConstraints();

        //插入超大标题
        gri.gridx=0;//表示当前在第几列
        gri.gridy=0;//表示当前在第几行
        gri.gridwidth=2;
        gri.insets=new Insets(0,5,30,5);
        gri.anchor=GridBagConstraints.CENTER;
        gri.fill = GridBagConstraints.NONE;
        background.add(name,gri);

        gri.insets=new Insets(10,5,10,5);
        //插入标题组件
        gri.gridx=0;//表示当前在第几列
        gri.gridy=1;//表示当前在第几行
        gri.gridwidth=2;
        gri.anchor=GridBagConstraints.CENTER;
        gri.fill = GridBagConstraints.NONE;
        background.add(title,gri);

        gri.fill= GridBagConstraints.BOTH;
        //插入account
        gri.gridx=0;//表示当前在第几列
        gri.gridy=2;//表示当前在第几行
        gri.gridwidth=1;
        gri.anchor=GridBagConstraints.EAST;
        background.add(account,gri);

        //插入输入框1
        gri.gridx=1;//表示当前在第几列
        gri.gridy=2;//表示当前在第几行
        gri.gridwidth=1;
        gri.gridheight=1;
        background.add(acc_text,gri);
        container.revalidate();

        //插入password
        gri.gridx=0;//表示当前在第几列
        gri.gridy=3;//表示当前在第几行
        gri.gridwidth=1;
        gri.gridheight=1;
        background.add(password,gri);
        container.revalidate();

        //插入输入框2
        gri.gridx=1;//表示当前在第几列
        gri.gridy=3;//表示当前在第几行
        gri.gridwidth=1;
        gri.gridheight=1;
        background.add(pas_text,gri);
        container.revalidate();

        //插入提交按钮
        gri.gridx=0;//表示当前在第几列
        gri.gridy=4;//表示当前在第几行
        gri.gridwidth=2;
        gri.gridheight=1;
        background.add(tip,gri);

        //插入提交按钮
        gri.gridx=0;//表示当前在第几列
        gri.gridy=5;//表示当前在第几行
        gri.gridwidth=2;
        gri.gridheight=1;
        //gri.anchor=GridBagConstraints.CENTER;
        background.add(submit,gri);

        container.revalidate();

        start();
    }

    public void start()//实现页面逻辑
    {
        submit_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//该函数会在点击登录按钮时执行

                if(ConnectDB.ExistAccount(account_v.getText(), new String(password_v.getPassword())))
                {
                    tip.setText("");
                    System.out.println("存在");
                    frame.dispose();//关闭当前窗体
                    MainMenu mainMenu=new MainMenu();
                    mainMenu.CreateMenu();
                }else {
                    if(Objects.equals(account_v.getText(), "") ||Objects.equals(new String(password_v.getPassword()),""))
                    {
                        tip.setText("用户名或密码为空");
                    }else{
                        tip.setText("用户名或密码错误");
                    }
                    System.out.println("不存在");
                }
            }
        });
    }


}
