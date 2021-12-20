package me.refluxo.bungee.util;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.connection.Player;
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
        ResultSet rs = new MySQLService().getResult("SELECT * FROM onlineTime WHERE uuid = '" + player.id().toString() + "';");
        if(!rs.next()) {
            new MySQLService().executeUpdate("INSERT INTO onlineTime(t,uuid) VALUES (0,'" + player.id().toString() + "');");
        }
    }

    public static void init() {
        ProxyServer proxyServer = Bungee.getProxyServer();
        new AsyncThread(() -> proxyServer.connectedPlayers().forEach(players -> {
            ResultSet rs = new MySQLService().getResult("SELECT * FROM onlineTime WHERE uuid = '" + players.id().toString() + "';");
            try {
                if (rs.next()) {
                    long time = rs.getLong("t");
                    new MySQLService().executeUpdate("UPDATE onlineTime SET t = " + (time+1) + " WHERE uuid = '" + players.id().toString() + "';");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        })).scheduleAsyncTask(0,20);
    }

}
