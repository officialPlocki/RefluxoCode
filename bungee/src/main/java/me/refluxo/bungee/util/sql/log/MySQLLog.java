package me.refluxo.bungee.util.sql.log;

import com.velocitypowered.api.proxy.Player;
import me.refluxo.bungee.util.sql.MySQLService;

public class MySQLLog {
    public void log(Player player, String log) {
        (new MySQLService()).executeUpdate("INSERT INTO log(i,log,t) VALUES ('player." + player.getUniqueId().toString() + "','" + log + "','" + System.currentTimeMillis() + "')");
    }

    public void log(String index, String log) {
        (new MySQLService()).executeUpdate("INSERT INTO log(i,log,t) VALUES ('" + index + "','" + log + "','" + System.currentTimeMillis() + "')");
    }
}
