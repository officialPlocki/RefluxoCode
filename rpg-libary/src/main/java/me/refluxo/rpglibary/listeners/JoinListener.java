package me.refluxo.rpglibary.listeners;

import me.refluxo.rpglibary.util.bar.StatsBar;
import me.refluxo.rpglibary.util.player.data.MySQLHealth;
import me.refluxo.rpglibary.util.player.data.MySQLPower;
import me.refluxo.serverlibary.util.player.PlayerAPI;
import me.refluxo.serverlibary.util.player.PlayerManager;
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
    }

}
