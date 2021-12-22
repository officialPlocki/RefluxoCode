package me.refluxo.bungee.util;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.refluxo.bungee.Bungee;
import me.refluxo.bungee.util.sql.MySQLService;
import xyz.plocki.asyncthread.AsyncThread;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OnlineTime {

    private final Player player;

    public OnlineTime(Player player) {
        this.player = player;
    }

    public void checkPlayer() throws SQLException {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM onlineTime WHERE uuid = '" + player.getUniqueId().toString() + "';");
        if(!rs.next()) {
            new MySQLService().executeUpdate("INSERT INTO onlineTime(t,uuid) VALUES (0,'" + player.getUniqueId().toString() + "');");
        }
    }

    public static void init() {
        ProxyServer proxyServer = Bungee.getProxyServer();
        new AsyncThread(() -> proxyServer.getAllPlayers().forEach(players -> {
            ResultSet rs = new MySQLService().getResult("SELECT * FROM onlineTime WHERE uuid = '" + players.getUniqueId().toString() + "';");
            try {
                if (rs.next()) {
                    long time = rs.getLong("t");
                    new MySQLService().executeUpdate("UPDATE onlineTime SET t = " + (time+1) + " WHERE uuid = '" + players.getUniqueId().toString() + "';");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        })).scheduleAsyncTask(0,20);
    }

}
