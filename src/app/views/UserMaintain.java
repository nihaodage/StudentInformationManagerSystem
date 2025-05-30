package app.views;

import app.models.ConnectDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.Objects;

public class UserMaintain {

    //子窗口
    static JInternalFrame frame=new JInternalFrame("用户信息维护", false, false, false, false);

    static JTextField command_t=new JTextField();
    static JTextField username_t=new JTextField();
    static JTextField password_t=new JTextField();


    static JButton addData=new JButton("添加");
    static JButton deleteData=new JButton("删除");
    static JButton exitData=new JButton("退出");

    static String[] columName={"用户ID","用户名","密码"};//列名
    static DefaultTableModel tableModel=new DefaultTableModel(columName,0){
        @Override
        public boolean isCellEditable(int row, int column) {//使单元格不能被编辑
            return false;
        }
    };
    static JTable table =new JTable(tableModel);//创建表格


    public static void show(JFrame menu){
        frame.getContentPane().removeAll();
        tableModel.setRowCount(0);//移除所有行
        //下面的代码负责生成子窗口
        JLayeredPane jLayeredPane=menu.getLayeredPane();//创建分层模版，使得子窗口在主窗口之上

        JDesktopPane jDesktopPane=new JDesktopPane();//创建桌面模版，使得子窗口可以随意拖动
        jDesktopPane.setBounds(0,0,menu.getWidth(),menu.getHeight());//设置桌面模版大小
        jDesktopPane.setOpaque(false);//使得桌面模版背景为透明

        //创建子窗口

        frame.setVisible(true);//使得可见
        frame.setSize(menu.getWidth()/2,menu.getHeight()/2+menu.getHeight()/8);//大小
        frame.setLocation(menu.getWidth()/2-frame.getWidth()/2,menu.getHeight()/2-frame.getHeight()/2);

        jDesktopPane.add(frame);//在桌面模板里面添加子窗口
        jLayeredPane.add(jDesktopPane,JLayeredPane.PALETTE_LAYER);//在分层模板里面添加桌面模板为顶层
        //上面的代码负责生成子窗口

        JPanel showInformation=new JPanel();
        showInformation.setBounds(0,0,frame.getWidth(),200);//创建显示信息板子
        showInformation.setLayout(new BorderLayout());



        //创建表格单

        ResultSet re=null;
        re=ConnectDB.Select("select * from user");
        if(re!=null)
        {
            try{
                while(re.next()) {
                    String command=re.getString("user_id");
                    String username=re.getString("username");
                    String password=re.getString("password");

                    Object[] rowData = {
                            command,username, password
                    };
                    tableModel.addRow(rowData);

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                        frame,    // 父组件（对话框将居中显示于此组件）
                        "数据库数据有误",  // 消息内容
                        "错误",               // 对话框标题
                        JOptionPane.WARNING_MESSAGE  // 消息类型（显示警告图标）
                );
            }
        }

        table.setRowHeight(30);
        table.getTableHeader().setReorderingAllowed(false);//使列名不能被重新排序

        JScrollPane scrollPane =new JScrollPane(table);//创建滚动条
        showInformation.add(scrollPane, BorderLayout.CENTER);
        frame.add(showInformation);

        command_t.setEditable(false);

        //创建按钮界面
        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gri=new GridBagConstraints();

        //创建对应标签
        JLabel command=new JLabel("用户ID");
        JLabel username=new JLabel("用户名");
        JLabel password=new JLabel("密码");



        // 创建第一个标签对象
        GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.gridx = 0;
        labelGbc.gridy = 0;
        labelGbc.weightx = 0.5; // 占水平的一半
        labelGbc.anchor = GridBagConstraints.WEST; // 左对齐标签
        labelGbc.insets = new Insets(5,5,5,5); // 统一边距
        buttonPanel.add(command, labelGbc);

        // 创建第一个文本框对象
        GridBagConstraints fieldGbc = new GridBagConstraints();
        fieldGbc.gridx = 1;
        fieldGbc.gridy = 0;
        fieldGbc.weightx = 0.5; // 占水平的一半
        fieldGbc.fill = GridBagConstraints.HORIZONTAL; // 水平填充
        fieldGbc.insets = new Insets(5,5,5,5);
        buttonPanel.add(command_t, fieldGbc);

        // 第二个标签
        GridBagConstraints nameLabelGbc = new GridBagConstraints();
        nameLabelGbc.gridx = 0;
        nameLabelGbc.gridy = 1; // 使用连续行号
        nameLabelGbc.weightx=0.5;
        nameLabelGbc.anchor = GridBagConstraints.WEST;
        nameLabelGbc.insets = new Insets(5,5,5,5);
        buttonPanel.add(username, nameLabelGbc);

        // 第二个文本框
        GridBagConstraints nameFieldGbc = new GridBagConstraints();
        nameFieldGbc.gridx = 1;
        nameFieldGbc.gridy = 1;
        nameFieldGbc.weightx = 0.5;
        nameFieldGbc.fill = GridBagConstraints.HORIZONTAL;
        nameFieldGbc.insets = new Insets(5,5,5,5);
        buttonPanel.add(username_t, nameFieldGbc);

        // 第三个标签
        nameLabelGbc.gridx = 0;
        nameLabelGbc.gridy = 2; // 使用连续行号
        nameLabelGbc.weightx=0.5;
        nameLabelGbc.anchor = GridBagConstraints.WEST;
        nameLabelGbc.insets = new Insets(5,5,5,5);
        buttonPanel.add(password, nameLabelGbc);

        // 第三个文本框
        nameFieldGbc.gridx = 1;
        nameFieldGbc.gridy = 2;
        nameFieldGbc.weightx = 0.5;
        nameFieldGbc.fill = GridBagConstraints.HORIZONTAL;
        nameFieldGbc.insets = new Insets(5,5,5,5);
        buttonPanel.add(password_t, nameFieldGbc);


        //创建按钮板子
        JPanel choosePanel=new JPanel();
        choosePanel.add(addData);
        choosePanel.add(deleteData);
        choosePanel.add(exitData);

        //将按钮板子加入按钮模版
        gri.gridx=0;
        gri.gridy=3;
        gri.gridwidth=2;
        gri.anchor=GridBagConstraints.CENTER;
        buttonPanel.add(choosePanel,gri);

        frame.add(buttonPanel,BorderLayout.SOUTH);

        menu.repaint();
        menu.revalidate();

        start();
    }
    public static void start()
    {
        ActionListener add=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username=username_t.getText();
                String password=password_t.getText();
                String command="";

                System.out.println(username);
                System.out.println(password);
                if(Objects.equals(username, "") || Objects.equals(password, ""))
                {
                    String error_message ="用户名或密码不能为空";
                    JOptionPane.showMessageDialog(
                            frame,    // 父组件（对话框将居中显示于此组件）
                            error_message,  // 消息内容
                            "错误",               // 对话框标题
                            JOptionPane.WARNING_MESSAGE  // 消息类型（显示警告图标）
                    );
                }else {
                    String sql=String.format("INSERT into user VALUES (%s,'%s','%s')","0",username,password);
                    ResultSet rs=ConnectDB.Select(String.format("select user_id from user where username='%s' and password='%s'",username,password));
                    try {
                        rs.next();
                        command=rs.getString("username");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    //如果sql执行出错，就提出警示框
                    SQLException t=ConnectDB.AddData(sql);
                    //将新添加的记录显示在屏幕上
                    Object[] rowData = {
                            command,username,password
                    };
                    tableModel.addRow(rowData);
                }

            }
        };
        addData.addActionListener(add);

        ActionListener delete=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index=table.getSelectedRow();//如果是-1表示未选择记录
                if(index==-1)
                {
                    JOptionPane.showMessageDialog(
                            frame,    // 父组件（对话框将居中显示于此组件）
                            "未选择一条记录！",  // 消息内容
                            "错误",               // 对话框标题
                            JOptionPane.WARNING_MESSAGE  // 消息类型（显示警告图标）
                    );
                }else {
                    String code= (String) table.getValueAt(index,0);//获取选择的这一行的学生编号
                    String sql="DELETE from user where user_id="+code;
                    ConnectDB.Delete(sql);
                    tableModel.removeRow(index);//在表格里删除这一行
                }
            }
        };
        deleteData.addActionListener(delete);

        ActionListener exit=new ActionListener() {//如果点击关闭按钮，就释放资源
            @Override
            public void actionPerformed(ActionEvent e) {
                addData.removeActionListener(add);//移除监听器
                deleteData.removeActionListener(delete);
                clear_textfiled();//清除文本框
                frame.dispose();//关闭窗口
            }
        };
        exitData.addActionListener(exit);//给关闭按钮加上事件监听
    }
    public static void clear_textfiled()//清除文本框
    {
        username_t.setText("");
        password_t.setText("");
    }
}