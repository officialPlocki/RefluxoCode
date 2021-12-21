package me.refluxo.rpglibrary.listeners;

import me.refluxo.rpglibrary.util.bar.StatsBar;
import me.refluxo.rpglibrary.util.player.data.MySQLHealth;
import me.refluxo.rpglibrary.util.player.data.MySQLPower;
import me.refluxo.rpglibrary.util.player.data.MySQLSkill;
import me.refluxo.serverlibrary.util.player.PlayerAPI;
import me.refluxo.serverlibrary.util.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerAPI player = new PlayerManager().getPlayer(event.getPlayer());
        new MySQLHealth(player).checkPlayer();
        new MySQLPower(player).checkPlayer();
        new StatsBar(player).checkPlayer();
        new MySQLSkill(player).checkPlayer();
    }

}
