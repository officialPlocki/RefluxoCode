package me.refluxo.lobbysystem.listeners;

import me.refluxo.lobbysystem.util.VisualGUI;
import me.refluxo.serverlibrary.util.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(VisualGUI.containsPlayer(new PlayerManager().getPlayer(event.getPlayer()))) {
            VisualGUI.remove(new PlayerManager().getPlayer(event.getPlayer()));
        }
    }

}
