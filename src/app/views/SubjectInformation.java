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

public class SubjectInformation {

    //子窗口
    static JInternalFrame frame=new JInternalFrame("学生信息", false, false, false, false);

    static JTextField code_t=new JTextField();
    static JTextField name_t=new JTextField();

    static JButton addData=new JButton("添加");
    static JButton deleteData=new JButton("删除");
    static JButton exitData=new JButton("退出");
    static JButton alterData=new JButton("修改");

    static String[] columName={"科目编码","科目名称"};//列名
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
        re=ConnectDB.Select("select * from subject_information");
        if(re!=null)
        {
            try{
                while(re.next()) {
                    String code=re.getString("subject_id");
                    String name=re.getString("subject_name");

                    Object[] rowData = {
                            code, name
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
        //buttonPanel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()/4));
        GridBagConstraints gri=new GridBagConstraints();

        //创建对应标签
        JLabel code=new JLabel("科目编号");
        JLabel name=new JLabel("科目姓名");

        //code_t.setPreferredSize(new Dimension(100,20));

        // 创建第一个标签对象
        GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.gridx = 0;
        labelGbc.gridy = 0;
        labelGbc.weightx = 0.5; // 占水平的一半
        labelGbc.anchor = GridBagConstraints.WEST; // 左对齐标签
        labelGbc.insets = new Insets(5,5,5,5); // 统一边距
        buttonPanel.add(code, labelGbc);

        // 创建第一个文本框对象
        GridBagConstraints fieldGbc = new GridBagConstraints();
        fieldGbc.gridx = 1;
        fieldGbc.gridy = 0;
        fieldGbc.weightx = 0.5; // 占水平的一半
        fieldGbc.fill = GridBagConstraints.HORIZONTAL; // 水平填充
        fieldGbc.insets = new Insets(5,5,5,5);
        buttonPanel.add(code_t, fieldGbc);

        // 第二个标签
        GridBagConstraints nameLabelGbc = new GridBagConstraints();
        nameLabelGbc.gridx = 0;
        nameLabelGbc.gridy = 1; // 使用连续行号
        nameLabelGbc.weightx=0.5;
        nameLabelGbc.anchor = GridBagConstraints.WEST;
        nameLabelGbc.insets = new Insets(5,5,5,5);
        buttonPanel.add(name, nameLabelGbc);

        // 第二个文本框
        GridBagConstraints nameFieldGbc = new GridBagConstraints();
        nameFieldGbc.gridx = 1;
        nameFieldGbc.gridy = 1;
        nameFieldGbc.weightx = 0.5;
        nameFieldGbc.fill = GridBagConstraints.HORIZONTAL;
        nameFieldGbc.insets = new Insets(5,5,5,5);
        buttonPanel.add(name_t, nameFieldGbc);


        //创建按钮板子
        JPanel choosePanel=new JPanel();
        choosePanel.add(addData);
        choosePanel.add(deleteData);
        choosePanel.add(alterData);
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

        ActionListener add =new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code=code_t.getText();
                String name=name_t.getText();

                String sql=String.format("INSERT into subject_information  VALUES (%s,'%s')",code,name);

                //如果sql执行出错，就提出警示框
                SQLException t=ConnectDB.AddData(sql);
                if(t!=null){
                    String error_message= "";
                    if(t instanceof SQLSyntaxErrorException)
                    {
                        error_message="科目编号不能为空";
                    }else if(t instanceof SQLIntegrityConstraintViolationException)
                    {
                        error_message="科目编号不能重复";
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
                            code, name
                    };
                    tableModel.addRow(rowData);
                    clear_textfiled();
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
                    String code= (String) table.getValueAt(index,0);//获取选择的这一行的科目编号
                    String sql="DELETE from subject_information where subject_id="+code;
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
                if(index==-1) {
                    JOptionPane.showMessageDialog(
                            frame,    // 父组件（对话框将居中显示于此组件）
                            "未选择行",  // 消息内容
                            "错误",               // 对话框标题
                            JOptionPane.WARNING_MESSAGE  // 消息类型（显示警告图标）
                    );
                }else{
                    String code_original=(String)table.getValueAt(index,0);
                    String code= Objects.equals(code_t.getText(), "") ?"Null":code_t.getText();
                    String name= Objects.equals(name_t.getText(), "") ?"Null":name_t.getText();

                    String sql=String.format("update subject_information set subject_id=%s,subject_name='%s' where subject_id=%s",code,name,code_original);
                    System.out.println(sql);

                    SQLException t=ConnectDB.AddData(sql);
                    if(t!=null) {
                        String error_message = "";
                        if (t instanceof SQLSyntaxErrorException) {
                            error_message = "科目编码不能为空";
                        } else if (t instanceof SQLIntegrityConstraintViolationException) {
                            error_message = "科目编号不能重复";
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

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index=table.getSelectedRow();
                if(index!=-1)
                {
                    code_t.setText((String) table.getValueAt(index,0));
                    name_t.setText((String) table.getValueAt(index,1));
                }
            }
        });

    }
    public static void clear_textfiled()//清除文本框
    {
        code_t.setText("");
        name_t.setText("");
        table.getSelectionModel().clearSelection();
    }

}
