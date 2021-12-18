package me.refluxo.serverlibary.listeners;

import me.refluxo.serverlibary.util.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        new PlayerManager().unregisterPlayer(event.getPlayer());
    }

}
