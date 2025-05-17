package app.models;
import java.sql.*;

public class ConnectDB {
    static Connection con=null;
    static Statement st=null;
    static ResultSet re=null;

    public void CreateConnect(){
        String url="jdbc:mysql://localhost:3306/account";
        String username="root";
        String password="123456";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection(url,username,password);
            st=con.createStatement();
        } catch (ClassNotFoundException e) {
            System.out.println("class_error");
        } catch (SQLException e) {
            System.out.println("connect_sql_error");
        }
    }

    public static boolean ExistAccount(String username,String password)//判断数据库中是否存在提供的用户
    {
        try {
            re = st.executeQuery("select * from user");
            while(re.next())
            {
                if(username.equals(re.getString("username"))&&password.equals(re.getString("password")))
                {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("find_user_sql_error");
        }
            return false;
    }

    //插入数据
    public static SQLException AddData(String sql){//提供sql语句，如果发生了异常返回0，否则返回1
        try {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            return e;
        }
        return null;
    }

    //执行select语句，出错返回空
    public static ResultSet Select(String sql){
        try{
            re=st.executeQuery(sql);
        } catch (SQLException e) {
            return null;
        }
        return re;
    }

    //删除记录
    public static void Delete(String sql){
        try{
            st.executeUpdate(sql);
        } catch (SQLException e) {
            return ;
        }
    }



}
