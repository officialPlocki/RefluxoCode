package me.refluxo.serverlibary.listeners;

import me.refluxo.serverlibary.util.player.PlayerAPI;
import me.refluxo.serverlibary.util.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        new PlayerManager().registerPlayer(new PlayerAPI(event.getPlayer()));
    }

}
