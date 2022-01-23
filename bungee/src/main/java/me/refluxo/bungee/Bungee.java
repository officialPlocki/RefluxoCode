package me.refluxo.bungee;

import me.refluxo.bungee.util.files.FileBuilder;
import me.refluxo.bungee.util.files.YamlConfiguration;
import me.refluxo.bungee.util.sql.MySQLService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import xyz.plocki.asyncthread.AsyncThread;

import java.sql.SQLException;

public class Bungee extends Plugin {

    private static ProxyServer server = null;

    @Override
    public void onEnable() {
        server = ProxyServer.getInstance();
        FileBuilder fb = new FileBuilder("config/libary/config.yml");
        YamlConfiguration yml = fb.getYaml();
        if(!fb.getFile().exists()) {
            yml.set("mysql.host", "0.0.0.0");
            yml.set("mysql.port", 3306);
            yml.set("mysql.database", "database");
            yml.set("mysql.user", "user");
            yml.set("mysql.password", "password");
            fb.save();
        }
        try {
            MySQLService.connect(yml.getString("mysql.host"), yml.getInt("mysql.port"), yml.getString("mysql.database"), yml.getString("mysql.user"), yml.getString("mysql.password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        MySQLService.disconnect();
        AsyncThread.stopTasks();
    }

    public static ProxyServer getProxyServer() { return server; }

}
