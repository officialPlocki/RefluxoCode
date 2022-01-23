package me.refluxo.bungee.util.sql.log;

import me.refluxo.bungee.util.sql.MySQLService;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MySQLLog {
    public void log(ProxiedPlayer player, String log) {
        (new MySQLService()).executeUpdate("INSERT INTO log(i,log,t) VALUES ('player." + player.getUniqueId().toString() + "','" + log + "','" + System.currentTimeMillis() + "')");
    }

    public void log(String index, String log) {
        (new MySQLService()).executeUpdate("INSERT INTO log(i,log,t) VALUES ('" + index + "','" + log + "','" + System.currentTimeMillis() + "')");
    }
}
