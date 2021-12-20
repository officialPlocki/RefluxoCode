package me.refluxo.rpglibary.listeners;

import me.refluxo.rpglibary.util.player.RPGPlayer;
import me.refluxo.serverlibary.util.player.PlayerAPI;
import me.refluxo.serverlibary.util.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.plocki.asyncthread.AsyncThread;

import java.util.HashMap;
import java.util.Map;

public class DamageListener implements Listener {

    public static Map<PlayerAPI, Long> lastDamage;

    public static void init() {
        lastDamage = new HashMap<>();
        new AsyncThread(() -> {
            lastDamage.forEach((playerAPI, time) -> {
                if(!((System.currentTimeMillis() - time) <= 10000)) {
                    RPGPlayer player = new RPGPlayer(playerAPI);
                    player.setHealth(player.getRPGPlayer().getHealth()+1);
                    if(player.getRPGPlayer().getHealth() > player.getRPGPlayer().getMaxHealth()) {
                        player.setHealth(player.getRPGPlayer().getMaxHealth());
                    }
                }
            });
            new PlayerManager().getOnlinePlayers().forEach(playerAPI -> {
                RPGPlayer player = new RPGPlayer(playerAPI);
                player.setPower(player.getRPGPlayer().getPower()+1);
                if(player.getRPGPlayer().getPower() > player.getRPGPlayer().getMaxPower()) {
                    player.setPower(player.getRPGPlayer().getMaxPower());
                }
            });
        }).scheduleAsyncTask(0, 20);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!event.isCancelled()) {
            if(event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                PlayerAPI playerAPI = new PlayerManager().getPlayer(player);
                RPGPlayer rpgPlayer = new RPGPlayer(playerAPI);
                double damage = event.getDamage();
                lastDamage.put(playerAPI, System.currentTimeMillis());
                event.setDamage(0D);
                rpgPlayer.setHealth(rpgPlayer.getRPGPlayer().getHealth()- ((int) damage));
                if(rpgPlayer.getRPGPlayer().getHealth() <= 0) {
                    event.getEntity().remove();
                }
            }
        }
    }

}
