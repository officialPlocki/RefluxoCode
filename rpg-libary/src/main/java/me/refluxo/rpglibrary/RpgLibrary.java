package me.refluxo.rpglibrary;

import me.refluxo.rpglibrary.commands.MenuCommand;
import me.refluxo.rpglibrary.commands.SkillsCommand;
import me.refluxo.rpglibrary.listeners.*;
import me.refluxo.rpglibrary.util.bar.StatsBar;
import me.refluxo.rpglibrary.util.player.data.MySQLHealth;
import me.refluxo.rpglibrary.util.player.data.MySQLPower;
import me.refluxo.rpglibrary.util.player.data.MySQLQuest;
import me.refluxo.rpglibrary.util.player.data.MySQLSkill;
import me.refluxo.rpglibrary.util.quests.Quest;
import me.refluxo.rpglibrary.util.quests.QuestManager;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerMoveEvent;
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
        MySQLQuest.init();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new DamageListener(), this);
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new QuitListener(), this);
        pluginManager.registerEvents(new RegainHealthListener(), this);
        pluginManager.registerEvents(new DoubleJumpListener(), this);
        pluginManager.registerEvents(new RespawnListener(), this);

        new MenuCommand().register();
        new SkillsCommand().register();
        new QuestManager().registerQuest(new Quest(
                1, "Name", "Desc", PlayerMoveEvent.class, event -> {
                    PlayerMoveEvent playerMoveEvent = (PlayerMoveEvent) event;

                    return false;
        }
        ));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        new MenuCommand().unregister();
        new SkillsCommand().unregister();
    }
}
