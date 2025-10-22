package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:sqlite:lost_items.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC"); // 드라이버 로드
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC 드라이버를 찾을 수 없습니다.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
