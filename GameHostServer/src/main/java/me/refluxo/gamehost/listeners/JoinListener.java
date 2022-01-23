package me.refluxo.gamehost.listeners;

import me.refluxo.gamehost.util.ServerInformationManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(new ServerInformationManager().getOwnerUUID().equals("")) {
            event.getPlayer().setOp(true);
            new ServerInformationManager().setOwnerUUID(event.getPlayer().getUniqueId().toString());
        }
    }

}
