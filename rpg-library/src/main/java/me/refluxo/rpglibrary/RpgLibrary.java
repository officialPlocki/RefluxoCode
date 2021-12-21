package me.refluxo.rpglibrary;

import me.refluxo.rpglibrary.commands.MenuCommand;
import me.refluxo.rpglibrary.commands.SkillsCommand;
import me.refluxo.rpglibrary.listeners.DamageListener;
import me.refluxo.rpglibrary.listeners.JoinListener;
import me.refluxo.rpglibrary.listeners.QuitListener;
import me.refluxo.rpglibrary.listeners.RegainHealthListener;
import me.refluxo.rpglibrary.util.bar.StatsBar;
import me.refluxo.rpglibrary.util.player.data.MySQLHealth;
import me.refluxo.rpglibrary.util.player.data.MySQLPower;
import me.refluxo.rpglibrary.util.player.data.MySQLSkill;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class RpgLibrary extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        StatsBar.init();
        MySQLHealth.init();
        MySQLPower.init();
        DamageListener.init();
        MySQLSkill.init();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new DamageListener(), this);
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new QuitListener(), this);
        pluginManager.registerEvents(new RegainHealthListener(), this);

        new MenuCommand().register();
        new SkillsCommand().register();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        new MenuCommand().unregister();
        new SkillsCommand().unregister();
    }
}
