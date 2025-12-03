package com.example.team_project_mp;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {
    private Connection con;
    public DBManager() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/team_project_mp", "root", "1234");
        System.out.println("데이터베이스 연결됨");
    }
    public Connection getConnection() {
        return con;
    }
    public void close() {
        try {
            if (con != null && !con.isClosed()) con.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
