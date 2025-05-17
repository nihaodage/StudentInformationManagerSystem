package app.views;

import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {
    static int width,height;//屏幕分辨率
    int frameWidth;
    int frameHeight;//窗体分辨率

    public void CreateFrame(){
        //获取显示器分辨率
        Toolkit toolkit =Toolkit.getDefaultToolkit();
        Dimension screenSize =toolkit.getScreenSize();
        width=(int)screenSize.getWidth();
        height=(int)screenSize.getHeight();
        frameWidth=width/4;//初始化窗体分辨率
        frameHeight=height/2;
        //创建窗体

        JFrame frame =new JFrame("登录");//定义一个窗体变量
        Container container=frame.getContentPane();//获取frame的容器

        container.setLayout(new FlowLayout(1));

        container.setBackground(Color.white);//设置窗体背景颜色

        frame.setVisible(true);//使窗体可视
        frame.setResizable(false);//使窗体不可改变大小
        frame.setBounds(width/2-frameWidth/2,height/2-frameHeight/2,frameWidth,frameHeight);//设置窗体的位置和大小

        Login log=new Login();//实例化登录界面
        log.ShowLogin(container,frame,frameWidth,frameHeight);//显示登录界面

    }
}
