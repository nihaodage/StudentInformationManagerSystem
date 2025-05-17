package app;

import app.models.ConnectDB;
import app.views.Menu;

/*
1.先进行数据库连接
2.弹出操作界面
3.用户进行操作
*/
public class Main {
    public static void main(String[] args) {
        ConnectDB con=new ConnectDB();
        con.CreateConnect();

        Menu menu=new Menu();
        menu.CreateFrame();

    }
}