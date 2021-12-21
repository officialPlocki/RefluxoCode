package me.refluxo.rpglibrary.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class SwapHandItemListener implements Listener {

    @EventHandler
    public void onSwapHandItem(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
        event.getPlayer().performCommand("menu");
    }

}
