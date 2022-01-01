package me.refluxo.gamehost.lobby.util;

import me.refluxo.serverlibrary.util.sql.MySQLService;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PrivateServerMySQL {

    private final Player player;

    public PrivateServerMySQL(Player player) {
        this.player = player;
        setupPlayer();
    }

    public void setupPlayer() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM privateServer WHERE playerUUID = '" + player.getUniqueId() + "';");
        try {
            if (!rs.next()) {
                new MySQLService().executeUpdate("INSERT INTO privateServer(playerUUID,instanceUUID,instanceName,instanceIsOnline) VALUES ('" + player.getUniqueId() + "','" + UUID.randomUUID() + "','',false);");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isInstanceOnline() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM privateServer WHERE playerUUID = '" + player.getUniqueId() + "';");
        try {
            if(rs.next()) {
                return rs.getBoolean("instanceIsOnline");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getInstanceName() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM privateServer WHERE playerUUID = '" + player.getUniqueId() + "';");
        try {
            if(rs.next()) {
                return rs.getString("instanceName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getInstanceUUID() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM privateServer WHERE playerUUID = '" + player.getUniqueId() + "';");
        try {
            if(rs.next()) {
                return rs.getString("instanceUUID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setInstanceUUID(String uuid) {
        new MySQLService().executeUpdate("UPDATE privateServer SET instanceUUID = '" + uuid +"' WHERE playerUUID = '" + player.getUniqueId() + "';");
    }

    public void setInstanceName(String name) {
        new MySQLService().executeUpdate("UPDATE privateServer SET instanceName = '" + name + "' WHERE playerUUID = '" + player.getUniqueId() + "';");
    }

    public void setInstanceOnline(boolean online) {
        new MySQLService().executeUpdate("UPDATE privateServer SET instanceIsOnline = " + online + " WHERE playerUUID = '" + player.getUniqueId() + "';");
    }

    public static void init() {
        new MySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS privateServer(playerUUID TEXT, instanceUUID TEXT, instanceName TEXT, instanceIsOnline BOOLEAN);");
    }

}
