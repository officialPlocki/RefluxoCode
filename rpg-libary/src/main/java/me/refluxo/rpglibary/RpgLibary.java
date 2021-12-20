package me.refluxo.rpglibary;

import me.refluxo.rpglibary.listeners.DamageListener;
import me.refluxo.rpglibary.listeners.JoinListener;
import me.refluxo.rpglibary.listeners.QuitListener;
import me.refluxo.rpglibary.listeners.RegainHealthListener;
import me.refluxo.rpglibary.util.bar.StatsBar;
import me.refluxo.rpglibary.util.player.data.MySQLHealth;
import me.refluxo.rpglibary.util.player.data.MySQLPower;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class RpgLibary extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        StatsBar.init();
        MySQLHealth.init();
        MySQLPower.init();
        DamageListener.init();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new DamageListener(), this);
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new QuitListener(), this);
        pluginManager.registerEvents(new RegainHealthListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
