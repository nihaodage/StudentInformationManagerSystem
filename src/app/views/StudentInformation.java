package app.views;

import app.models.ConnectDB;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.Objects;

public class StudentInformation {

    //子窗口
    static JInternalFrame frame=new JInternalFrame("学生信息", false, false, false, false);

    static JTextField code_t=new JTextField();
    static JTextField name_t=new JTextField();
    static JTextField age_t=new JTextField();
    static JTextField telephone_t=new JTextField();
    static JTextField address_t=new JTextField();
    static JComboBox<String> gender_t=new JComboBox<>();

    static JButton addData=new JButton("添加");
    static JButton deleteData=new JButton("删除");
    static JButton exitData=new JButton("退出");
    static JButton alterData=new JButton("修改");

    static String[] columName={"学生编码","学生姓名","年龄","性别","联系电话","家庭地址"};//列名
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
        gender_t.removeAllItems();//移除所有选项
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
        re=ConnectDB.Select("select * from student_information");
        if(re!=null)
        {
            try{
                while(re.next()) {
                    String code=re.getString("student_id");
                    String name=re.getString("student_name");
                    String age=re.getString("age");
                    String gender=re.getString("gender");
                    String telephone=re.getString("telephone");
                    String address=re.getString("address");

                    Object[] rowData = {
                            code, name, age,
                            gender,telephone,address
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
        JLabel code=new JLabel("学生编号");
        JLabel name=new JLabel("学生姓名");
        JLabel age=new JLabel("年龄");
        JLabel gender=new JLabel("性别");
        JLabel telephone=new JLabel("联系电话");
        JLabel address=new JLabel("家庭地址");

        //调整样式
        code_t.setPreferredSize(new Dimension(80,20));
        name_t.setPreferredSize(new Dimension(80,20));
        age_t.setPreferredSize(new Dimension(80,20));
        telephone_t.setPreferredSize(new Dimension(80,20));
        address_t.setPreferredSize(new Dimension(80,20));
        gender_t.addItem("男");
        gender_t.addItem("女");

        //将标签和文本框绑定
        JPanel codePanel=new JPanel();
        codePanel.add(code);
        codePanel.add(code_t);

        JPanel namePanel=new JPanel();
        namePanel.add(name);
        namePanel.add(name_t);

        JPanel agePanel=new JPanel();
        agePanel.add(age);
        agePanel.add(age_t);

        JPanel genderPanel=new JPanel();
        genderPanel.add(gender);
        genderPanel.add(gender_t);

        JPanel telephonePanel=new JPanel();
        telephonePanel.add(telephone);
        telephonePanel.add(telephone_t);

        JPanel addressPanel=new JPanel();
        addressPanel.add(address);
        addressPanel.add(address_t);

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
        buttonPanel.add(agePanel,gri);

        gri.gridx=6;
        gri.gridy=0;
        gri.gridwidth=3;
        buttonPanel.add(genderPanel,gri);

        gri.gridx=0;
        gri.gridy=1;
        gri.gridwidth=2;
        buttonPanel.add(telephonePanel,gri);

        gri.gridx=2;
        gri.gridy=1;
        gri.gridwidth=2;
        buttonPanel.add(addressPanel,gri);

        //创建按钮板子
        JPanel choosePanel=new JPanel();
        choosePanel.add(addData);
        choosePanel.add(deleteData);
        choosePanel.add(alterData);
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
                String age=age_t.getText();
                String gender= (String) gender_t.getSelectedItem();
                String telephone=telephone_t.getText();
                String address=address_t.getText();

                String sql=String.format("INSERT into student_information  VALUES (%s,'%s','%s','%s','%s','%s')",code,name,age,gender,telephone,address);

                //如果sql执行出错，就提出警示框
                SQLException t=ConnectDB.AddData(sql);
                if(t!=null){
                    String error_message= "";
                    if(t instanceof SQLSyntaxErrorException)
                    {
                        error_message="学生编码不能为空";
                    }else if(t instanceof SQLIntegrityConstraintViolationException)
                    {
                        error_message="学生编号不能重复";
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
                            code, name, age,
                            gender,telephone,address
                    };
                    tableModel.addRow(rowData);
                }
                clear_textfiled();
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
                    String sql="DELETE from student_information where student_id="+code;
                    ConnectDB.Delete(sql);
                    tableModel.removeRow(index);//在表格里删除这一行
                }
                clear_textfiled();
            }
        };
        deleteData.addActionListener(delete);

        ActionListener alter=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index=table.getSelectedRow();
                if(index==-1)
                {
                    JOptionPane.showMessageDialog(
                            frame,    // 父组件（对话框将居中显示于此组件）
                            "未选择行",  // 消息内容
                            "错误",               // 对话框标题
                            JOptionPane.WARNING_MESSAGE  // 消息类型（显示警告图标）
                    );
                }else {
                    String code_orignal=(String)table.getValueAt(index,0);
                    String code= Objects.equals(code_t.getText(), "") ?"Null":code_t.getText();
                    String name= Objects.equals(name_t.getText(), "") ?"Null":name_t.getText();
                    String age= Objects.equals(age_t.getText(), "") ?"Null":age_t.getText();
                    String gender= (String) gender_t.getSelectedItem();
                    String telephone= Objects.equals(telephone_t.getText(), "") ?"Null":telephone_t.getText();
                    String address= Objects.equals(address_t.getText(), "") ?"Null":address_t.getText();

                    String sql=String.format("update student_information set student_id=%s,student_name='%s',age='%s',gender='%s',telephone=%s,address='%s' where student_id=%s",code,name,age,gender,telephone,address,code_orignal);
                    System.out.println(sql);

                    SQLException t=ConnectDB.AddData(sql);
                    if(t!=null) {
                        String error_message = "";
                        if (t instanceof SQLSyntaxErrorException) {
                            error_message = "学生编码不能为空";
                        } else if (t instanceof SQLIntegrityConstraintViolationException) {
                            error_message = "学生编号不能重复";
                        }

                        JOptionPane.showMessageDialog(
                                frame,    // 父组件（对话框将居中显示于此组件）
                                error_message,  // 消息内容
                                "错误",               // 对话框标题
                                JOptionPane.WARNING_MESSAGE  // 消息类型（显示警告图标）
                        );
                    }else {
                        tableModel.setValueAt(code,index,0);
                        tableModel.setValueAt(name,index,1);
                        tableModel.setValueAt(age,index,2);
                        tableModel.setValueAt(gender,index,3);
                        tableModel.setValueAt(telephone,index,4);
                        tableModel.setValueAt(address,index,5);

                    }
                    clear_textfiled();
                }
            }
        };
        alterData.addActionListener(alter);

        ActionListener exit=new ActionListener() {//如果点击关闭按钮，就释放资源
            @Override
            public void actionPerformed(ActionEvent e) {
                addData.removeActionListener(add);//移除监听器
                deleteData.removeActionListener(delete);
                alterData.removeActionListener(alter);
                clear_textfiled();//清除文本框
                frame.dispose();//关闭窗口
            }
        };
        exitData.addActionListener(exit);//给关闭按钮加上事件监听

        //给表格选择行模式添加监听器，如果选择行就将数据填入文本框中
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index=table.getSelectedRow();
                if(index!=-1)
                {
                    code_t.setText((String) table.getValueAt(index,0));
                    name_t.setText((String) table.getValueAt(index,1));
                    age_t.setText((String) table.getValueAt(index,2));
                    gender_t.setSelectedItem(table.getValueAt(index,3));
                    telephone_t.setText((String) table.getValueAt(index,4));
                    address_t.setText((String) table.getValueAt(index,5));
                }
            }
        });





    }
    public static void clear_textfiled()//清除文本框
    {
        code_t.setText("");
        age_t.setText("");
        gender_t.setSelectedItem("男");
        telephone_t.setText("");
        address_t.setText("");
        name_t.setText("");
        table.getSelectionModel().clearSelection();
    }

}
