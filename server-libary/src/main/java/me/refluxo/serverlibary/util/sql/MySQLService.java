package me.refluxo.serverlibary.util.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLService {

    private static Connection con;

    public static void connect(String host, int port, String database, String username, String password) throws SQLException {
        if(!isConnected()) {
            con = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, username, password);
        }
    }

    public static void disconnect() {
        if(isConnected()) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnected() {
        try {
            return !con.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void executeUpdate(String sql) {
        try {
            con.prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object getResult(String sql, String index, Class<?> clazz) {
        try {
            return con.prepareStatement(sql).executeQuery().getObject(index, clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
