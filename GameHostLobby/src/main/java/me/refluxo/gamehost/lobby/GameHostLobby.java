package me.refluxo.gamehost.lobby;

import me.refluxo.gamehost.lobby.commands.PrivateServerCommand;
import me.refluxo.gamehost.lobby.commands.PrivateServerListCommand;
import me.refluxo.gamehost.lobby.listeners.InventoryClickListener;
import me.refluxo.gamehost.lobby.util.PrivateServerConfigurationMySQL;
import me.refluxo.gamehost.lobby.util.PrivateServerMySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class GameHostLobby extends JavaPlugin {

    public static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        PrivateServerConfigurationMySQL.init();
        PrivateServerMySQL.init();
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        this.getCommand("ps").setExecutor(new PrivateServerCommand());
        this.getCommand("pslist").setExecutor(new PrivateServerListCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
