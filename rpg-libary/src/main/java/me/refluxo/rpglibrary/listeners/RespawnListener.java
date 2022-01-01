package me.refluxo.rpglibrary.listeners;

import me.refluxo.rpglibrary.util.player.RPGPlayer;
import me.refluxo.serverlibrary.util.player.PlayerAPI;
import me.refluxo.serverlibrary.util.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        PlayerAPI player = new PlayerManager().getPlayer(event.getPlayer());
        new RPGPlayer(player).setPower(new RPGPlayer(player).getRPGPlayer().getMaxPower());
        new RPGPlayer(player).setHealth(new RPGPlayer(player).getRPGPlayer().getMaxHealth());
    }

}
