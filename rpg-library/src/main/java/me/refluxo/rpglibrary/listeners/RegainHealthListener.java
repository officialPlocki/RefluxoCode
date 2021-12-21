package me.refluxo.rpglibrary.listeners;

import me.refluxo.rpglibrary.util.player.RPGPlayer;
import me.refluxo.serverlibrary.util.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class RegainHealthListener implements Listener {

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent event) {
        if(event.getEntity() instanceof Player) {
            RPGPlayer player = new RPGPlayer(new PlayerManager().getPlayer((Player) event.getEntity()));
            player.setHealth(player.getRPGPlayer().getHealth()+1);
            if(player.getRPGPlayer().getHealth() > player.getRPGPlayer().getMaxHealth()) {
                player.setHealth(player.getRPGPlayer().getMaxHealth());
            }
        }
    }

}
