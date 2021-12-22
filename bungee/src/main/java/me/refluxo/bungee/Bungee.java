package me.refluxo.bungee;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import me.refluxo.bungee.listeners.JoinEvent;
import me.refluxo.bungee.listeners.PingEvent;
import me.refluxo.bungee.util.OnlineTime;
import me.refluxo.bungee.util.files.FileBuilder;
import me.refluxo.bungee.util.files.YamlConfiguration;
import me.refluxo.bungee.util.sql.MySQLService;
import org.slf4j.Logger;

import java.sql.SQLException;

@Plugin(
        id = "bungee",
        name = "Bungee",
        version = "1.0-SNAPSHOT"
)
public class Bungee {

    private static Logger logger = null;
    private static ProxyServer server = null;

    @Inject
    public Bungee(ProxyServer server, Logger logger) {
        Bungee.server = server;
        Bungee.logger = logger;
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
        server.getEventManager().register(this, new JoinEvent());
        server.getEventManager().register(this, new PingEvent());
        OnlineTime.init();
    }

    @Subscribe
    public void onDisable(ProxyShutdownEvent event) {
        MySQLService.disconnect();
    }

    @SuppressWarnings("unused")
    public static Logger getLogger() { return logger; }

    public static ProxyServer getProxyServer() { return server; }

    @SuppressWarnings("unused")
    public static ProxyServer getInstance() {
        return server;
    }

}
