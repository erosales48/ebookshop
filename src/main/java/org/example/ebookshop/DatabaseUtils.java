package org.example.ebookshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {

    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";

    public static void loadMySQLDriver() throws  ClassNotFoundException{
        Class.forName(DB_DRIVER);
    }

    public static Connection getConnection(String url, String user, String password) throws SQLException, ClassNotFoundException {
        loadMySQLDriver();
        return DriverManager.getConnection(url, user, password);
    }
}
