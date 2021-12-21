package me.refluxo.rpglibrary.util.player.data;

import me.refluxo.serverlibrary.util.player.PlayerAPI;
import me.refluxo.serverlibrary.util.sql.MySQLService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLPower {

    public static void init() {
        new MySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS rpgPower(uuid TEXT, power INT, maxPower INT);");
    }
    
    private final PlayerAPI player;
    
    public MySQLPower(PlayerAPI playerAPI) {
        player = playerAPI;
    }
    
    public void checkPlayer() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM rpgPower WHERE uuid = '" + player.getAPIPlayer().getUUID() + "';");
        try {
            if(!rs.next()) {
                new MySQLService().executeUpdate("INSERT INTO rpgPower(uuid,power,maxPower) VALUES ('" + player.getAPIPlayer().getUUID() + "',20,20);");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int getPower() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM rpgPower WHERE uuid = '" + player.getAPIPlayer().getUUID() + "';");
        try {
            if(rs.next()) {
                return rs.getInt("power");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 20;
    }
    
    public int getMaxPower() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM rpgPower WHERE uuid = '" + player.getAPIPlayer().getUUID() + "';");
        try {
            if(rs.next()) {
                return rs.getInt("maxPower");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 20;
    }
    
    public void setPower(int power) {
        new MySQLService().executeUpdate("UPDATE rpgPower SET power = " + power + " WHERE uuid = '" + player.getAPIPlayer().getUUID() + "';");
    }
    
    public void setMaxPower(int power) {
        new MySQLService().executeUpdate("UPDATE rpgPower SET maxPower = " + power + " WHERE uuid = '" + player.getAPIPlayer().getUUID() + "';");
    }
    
}
