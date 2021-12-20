package me.refluxo.rpglibary.listeners;

import me.refluxo.rpglibary.util.bar.StatsBar;
import me.refluxo.serverlibary.util.player.PlayerAPI;
import me.refluxo.serverlibary.util.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlayerAPI player = new PlayerManager().getPlayer(event.getPlayer());
        new StatsBar(player).removePlayer();
    }

}
