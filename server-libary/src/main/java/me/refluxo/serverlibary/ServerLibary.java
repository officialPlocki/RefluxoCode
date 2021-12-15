package me.refluxo.serverlibary;

import me.refluxo.serverlibary.util.files.FileBuilder;
import me.refluxo.serverlibary.util.files.YamlConfiguration;
import me.refluxo.serverlibary.util.sql.MySQLService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class ServerLibary extends JavaPlugin {

    private static Plugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        FileBuilder fb = new FileBuilder("config/libary/config.yml");
        YamlConfiguration yml = fb.getYaml();
        Bukkit.getConsoleSender().sendMessage("Checking config...");
        if(!fb.getFile().exists()) {
            Bukkit.getConsoleSender().sendMessage("Config file doesn't exist, generating now...");
            yml.set("mysql.host", "0.0.0.0");
            yml.set("mysql.port", 3306);
            yml.set("mysql.database", "database");
            yml.set("mysql.user", "user");
            yml.set("mysql.password", "password");
            fb.save();
            Bukkit.getConsoleSender().sendMessage("Config file generated, please configure.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        Bukkit.getConsoleSender().sendMessage("Config file checked, trying to connect to MySQL.");
        try {
            MySQLService.connect(yml.getString("mysql.host"), yml.getInt("mysql.port"), yml.getString("mysql.database"), yml.getString("mysql.user"), yml.getString("mysql.password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(MySQLService.isConnected()) {
            Bukkit.getConsoleSender().sendMessage("Connected to MySQL.");
        } else {
            Bukkit.getConsoleSender().sendMessage("Can't connect to MySQL, is it offline (or is the config not configured)? Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        plugin = this;
        Bukkit.getConsoleSender().sendMessage("Registering commands...");
        //commands




        Bukkit.getConsoleSender().sendMessage("Commands registered. Trying to load listeners...");
        //listeners


        Bukkit.getConsoleSender().sendMessage("Listeners loaded.");
        Bukkit.getConsoleSender().sendMessage("Libary loaded. JettPack can now load plugins that depends on this Libary.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage("Saving settings...");
        //settings

        Bukkit.getConsoleSender().sendMessage("Disconnecting MySQL...");
        MySQLService.disconnect();
    }

    public static Plugin getPlugin() {
        return plugin;
    }

}
