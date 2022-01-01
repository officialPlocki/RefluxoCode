package me.refluxo.lobbysystem;

import me.refluxo.lobbysystem.commands.SetLocationCommand;
import me.refluxo.lobbysystem.listeners.BuildListener;
import me.refluxo.lobbysystem.listeners.InteractListener;
import me.refluxo.lobbysystem.listeners.JoinListener;
import me.refluxo.lobbysystem.util.VisualGUI;
import me.refluxo.serverlibrary.util.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class LobbySystem extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        VisualGUI.init();
        Bukkit.getPluginManager().registerEvents(new BuildListener(), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new VisualGUI(), this);
        Objects.requireNonNull(this.getCommand("setlocation")).setExecutor(new SetLocationCommand());
    }

    @Override
    public void onDisable() {
        VisualGUI.players.forEach(player -> {
            VisualGUI.remove(new PlayerManager().getPlayer(player));
        });
    }
}
