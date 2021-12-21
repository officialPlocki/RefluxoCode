package me.refluxo.rpglibrary.listeners;

import me.refluxo.rpglibrary.util.bar.StatsBar;
import me.refluxo.serverlibrary.util.player.PlayerAPI;
import me.refluxo.serverlibrary.util.player.PlayerManager;
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
