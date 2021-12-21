package me.refluxo.serverlibrary;

import me.refluxo.serverlibrary.listeners.ChatEvent;
import me.refluxo.serverlibrary.listeners.JoinEvent;
import me.refluxo.serverlibrary.listeners.QuitEvent;
import me.refluxo.serverlibrary.util.files.FileBuilder;
import me.refluxo.serverlibrary.util.files.YamlConfiguration;
import me.refluxo.serverlibrary.util.player.bad.BadWords;
import me.refluxo.serverlibrary.util.score.scoreboard.ScoreboardAPI;
import me.refluxo.serverlibrary.util.sql.MySQLService;
import me.refluxo.serverlibrary.util.score.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.plocki.asyncthread.AsyncThread;

import java.sql.SQLException;

public final class ServerLibrary extends JavaPlugin {

    private static Plugin plugin;

    public static String prefix = "§b§lRef§f§lluxo§c§l.me §8» §7";

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager pm = Bukkit.getPluginManager();
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
        new MySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS log(i TEXT, log TEXT, t TEXT);");
        plugin = this;
        Bukkit.getConsoleSender().sendMessage("Registering commands...");
        //commands



        Bukkit.getConsoleSender().sendMessage("Commands registered. Trying to load listeners...");
        //listeners
        pm.registerEvents(new JoinEvent(), this);
        pm.registerEvents(new ChatEvent(), this);
        pm.registerEvents(new QuitEvent(), this);

        Bukkit.getConsoleSender().sendMessage("Listeners loaded. Registering outgoing messaging channel...");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        new AsyncThread(BadWords::init).runAsync();
        ScoreboardAPI.init();

        RankManager rm = new RankManager();
        rm.registerRank("administrator", "§4Admin §8✰ §7", " §c§lTeam");
        rm.registerRank("developer", "§bDev §8✰ §7", " §c§lTeam");
        rm.registerRank("content", "§3Content §8✰ §7", " §c§lTeam");
        rm.registerRank("designer", "§1Designer §8✰ §7", " §c§lTeam");
        rm.registerRank("moderator", "§cMod §8✰ §7", " §c§lTeam");
        rm.registerRank("builder", "§aBuilder §8✰ §7", " §c§lTeam");
        rm.registerRank("supporter", "§eSup §8✰ §7", " §c§lTeam");
        rm.registerRank("freund", "§bFreund §8✰ §7", "");
        rm.registerRank("promoter", "§dPromo §8✰ §7", "");
        rm.registerRank("premium", "§6Prem §8✰ §7", "");
        rm.registerRank("spieler", "§7Spieler §8✰ §7", "");

        Bukkit.getConsoleSender().sendMessage("Channel loaded. JettPack can now load plugins that depends on this Libary.");
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
