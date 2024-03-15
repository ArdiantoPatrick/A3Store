package DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;

public class DBConnect {
    public Connection conn;
    public Statement stat;
    public ResultSet result;
    public PreparedStatement pstat;

    public DBConnect() {
        try {
            String url = "jdbc:sqlserver://localhost;database=A3Store;user=sa;password=12345";
            conn = DriverManager.getConnection(url);
            stat = conn.createStatement();
        }
        catch (Exception e){
            System.out.println("Error saat connect database : "+e);
        }
    }

    public static void main(String[] args) {
        DBConnect connection = new DBConnect();
        System.out.println("Connection Berhasil");
    }
}
