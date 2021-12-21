package me.refluxo.serverlibrary.util.sql.log;

import me.refluxo.serverlibrary.util.sql.MySQLService;
import org.bukkit.entity.Player;

public class MySQLLog {

    public void log(Player player, String log) {
        new MySQLService().executeUpdate("INSERT INTO log(i,log,t) VALUES ('player."+player.getUniqueId()+"','"+log+"','"+System.currentTimeMillis()+"')");
    }

    public void log(String index, String log) {
        new MySQLService().executeUpdate("INSERT INTO log(i,log,t) VALUES ('"+index+"','"+log+"','"+System.currentTimeMillis()+"')");
    }

}
