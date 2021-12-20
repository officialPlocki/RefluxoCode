package me.refluxo.rpglibary.util.player.data;

import me.refluxo.serverlibary.util.player.APIPlayer;
import me.refluxo.serverlibary.util.player.PlayerAPI;
import me.refluxo.serverlibary.util.sql.MySQLService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLHealth {

    public static void init() {
        new MySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS rpgHealth(uuid TEXT, health INT, maxHealth INT);");
    }

    private final PlayerAPI player;

    public MySQLHealth(PlayerAPI playerAPI) {
        player = playerAPI;
    }

    public void checkPlayer() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM rpgHealth WHERE uuid = '" + player.getAPIPlayer().getUUID() + "';");
        try {
            if(!rs.next()) {
                new MySQLService().executeUpdate("INSERT INTO rpgHealth(uuid,health,maxHealth) VALUES ('" + player.getAPIPlayer().getUUID() + "',20,20);");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getHealth() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM rpgHealth WHERE uuid = '" + player.getAPIPlayer().getUUID() + "';");
        try {
            if(rs.next()) {
                return rs.getInt("health");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 20;
    }

    public int getMaxHealth() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM rpgHealth WHERE uuid = '" + player.getAPIPlayer().getUUID() + "';");
        try {
            if(rs.next()) {
                return rs.getInt("maxHealth");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 20;
    }

    public void setHealth(int health) {
        new MySQLService().executeUpdate("UPDATE rpgHealth SET health = " + health + " WHERE uuid = '" + player.getAPIPlayer().getUUID() + "';");
    }

    public void setMaxHealth(int health) {
        new MySQLService().executeUpdate("UPDATE rpgHealth SET maxHealth = " + health + " WHERE uuid = '" + player.getAPIPlayer().getUUID() + "';");
    }

}
