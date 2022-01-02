package me.refluxo.gamehost.listeners;

import de.dytanic.cloudnet.driver.module.driver.DriverModule;
import me.refluxo.serverlibrary.util.player.PlayerAPI;
import me.refluxo.serverlibrary.util.player.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(event.getPlayer().getUniqueId().toString().equals(new DriverModule().getConfig().getString("ownerUUID"))) {
            if(!event.getPlayer().isOp()) {
                event.getPlayer().setOp(true);
                new PlayerManager().getPlayer(event.getPlayer()).sendMessage(PlayerAPI.MessageType.NORMAL, ChatMessageType.CHAT, "Du kannst Änderungen am Server mit /ps vornehmen.");
            }
        }
    }

}
