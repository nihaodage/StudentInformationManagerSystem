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

public class TeacherInformation {
    //子窗口
    static JInternalFrame frame=new JInternalFrame("教师信息", false, false, false, false);

    static JTextField code_t=new JTextField();
    static JTextField name_t=new JTextField();
    static JComboBox<String> gender_t=new JComboBox<>();
    static JComboBox<String> status_t=new JComboBox<>();

    static JButton addData=new JButton("添加");
    static JButton deleteData=new JButton("删除");
    static JButton exitData=new JButton("退出");

    static String[] columName={"教师编码","教师姓名","性别","教师职称"};//列名
    static DefaultTableModel tableModel=new DefaultTableModel(columName,0){
        @Override
        public boolean isCellEditable(int row, int column) {//使单元格不能被编辑
            return false;
        }
    };
    static JTable table =new JTable(tableModel);//创建表格


    public static void show(JFrame menu)
    {
        frame.getContentPane().removeAll();
        tableModel.setRowCount(0);//移除所有行
        gender_t.removeAllItems();//移除下拉框的所有选
        status_t.removeAllItems();
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
        //上面的代码负责生成子窗口

        JPanel showInformation=new JPanel();
        showInformation.setBounds(0,0,frame.getWidth(),200);//创建显示信息板子
        showInformation.setLayout(new BorderLayout());

        //创建表格单
        ResultSet re=null;
        re= ConnectDB.Select("select * from teacher_information");
        if(re!=null)
        {
            try{
                while(re.next()) {
                    String code=re.getString("teacher_id");
                    String name=re.getString("teacher_name");
                    String gender=re.getString("gender");
                    String teacher_status=re.getString("teacher_status");

                    Object[] rowData = {
                            code, name,gender,teacher_status
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

        //创建按钮界面
        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()/4));
        GridBagConstraints gri=new GridBagConstraints();

        //创建对应标签
        JLabel code=new JLabel("教师编号");
        JLabel name=new JLabel("教师姓名");
        JLabel gender=new JLabel("性别");
        JLabel status=new JLabel("教师职称");

        //调整样式
        code_t.setPreferredSize(new Dimension(80,20));
        name_t.setPreferredSize(new Dimension(80,20));

        //给下拉框添加内容
        gender_t.addItem("男");
        gender_t.addItem("女");

        status_t.addItem("高级教师");
        status_t.addItem("中级教师");
        status_t.addItem("初级教师");

        //将标签和文本框绑定
        JPanel codePanel=new JPanel();
        codePanel.add(code);
        codePanel.add(code_t);

        JPanel namePanel=new JPanel();
        namePanel.add(name);
        namePanel.add(name_t);


        JPanel genderPanel=new JPanel();
        genderPanel.add(gender);
        genderPanel.add(gender_t);

        JPanel statusPanel=new JPanel();
        statusPanel.add(status);
        statusPanel.add(status_t);

        //将绑定后的加入按钮界面
        gri.gridx=0;
        gri.gridy=0;
        gri.gridwidth=2;
        buttonPanel.add(codePanel,gri);

        gri.gridx=2;
        gri.gridy=0;
        gri.gridwidth=2;
        buttonPanel.add(namePanel,gri);

        gri.gridx=4;
        gri.gridy=0;
        gri.gridwidth=2;
        buttonPanel.add(genderPanel,gri);

        gri.gridx=6;
        gri.gridy=0;
        gri.gridwidth=0;
        buttonPanel.add(statusPanel,gri);

        //创建按钮板子
        JPanel choosePanel=new JPanel();
        choosePanel.add(addData);
        choosePanel.add(deleteData);
        choosePanel.add(exitData);

        //将按钮板子加入按钮模版
        gri.gridx=5;
        gri.gridy=3;
        buttonPanel.add(choosePanel,gri);

        frame.add(buttonPanel,BorderLayout.SOUTH);

        menu.repaint();
        menu.revalidate();

        start();
    }

    public static void start()
    {

        ActionListener add =new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code=code_t.getText();
                String name=name_t.getText();
                String gender= (String)gender_t.getSelectedItem();
                String status=(String)status_t.getSelectedItem();

                String sql=String.format("INSERT into teacher_information  VALUES (%s,'%s','%s','%s')",code,name,gender,status);

                //如果sql执行出错，就提出警示框
                SQLException t=ConnectDB.AddData(sql);
                if(t!=null){
                    String error_message= "";
                    if(t instanceof SQLSyntaxErrorException)
                    {
                        error_message="教师编码不能为空";
                    }else if(t instanceof SQLIntegrityConstraintViolationException)
                    {
                        error_message="教师编号不能重复";
                    }
                    JOptionPane.showMessageDialog(
                            frame,    // 父组件（对话框将居中显示于此组件）
                            error_message,  // 消息内容
                            "错误",               // 对话框标题
                            JOptionPane.WARNING_MESSAGE  // 消息类型（显示警告图标）
                    );
                }else {
                    //将新添加的记录显示在屏幕上
                    Object[] rowData = {
                            code, name,
                            gender,status
                    };
                    tableModel.addRow(rowData);
                }
            }
        };
        addData.addActionListener(add);//给添加按钮加上事件监听

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
                    String sql="DELETE from teacher_information where teacher_id="+code;
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
        code_t.setText("");
        name_t.setText("");
    }
}
