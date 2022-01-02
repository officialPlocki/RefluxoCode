package me.refluxo.gamehost;

import me.refluxo.gamehost.commands.PrivateServerCommand;
import me.refluxo.gamehost.listeners.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Gamehost extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("ps").setExecutor(new PrivateServerCommand());
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
