package me.refluxo.serverlibrary.util.sql;

import me.refluxo.serverlibrary.util.player.PlayerAPI;
import org.bukkit.entity.Player;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CoinsAPI {

    private Player player;

    public CoinsAPI(Player player) {
        this.player = player;
        checkPlayer();
    }

    private void checkPlayer() {
        if(getCoins() == -1) {
            new MySQLService().executeUpdate("INSERT INTO coins(uuid,coins) VALUES ('" + player.getUniqueId() + "',0);");
        }
    }

    public int getCoins() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM coins WHERE uuid = '" + player.getUniqueId() + "';");
        try {
            if(rs.next()) {
                return rs.getInt("coins");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void removeCoins(int coins) {
        setCoins(getCoins()-coins);
    }

    public void addCoins(int coins) {
        setCoins(getCoins()+coins);
    }

    public void setCoins(int coins) {
        new MySQLService().executeUpdate("UPDATE coins SET coins = " + coins + " WHERE uuid = '" + player.getUniqueId() + "';");
    }

    public static void init() {
        new MySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS coins(uuid TEXT, coins BIGINT);");
    }

}
