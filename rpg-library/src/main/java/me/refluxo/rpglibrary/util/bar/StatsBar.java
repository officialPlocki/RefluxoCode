package me.refluxo.rpglibrary.util.bar;

import me.refluxo.rpglibrary.util.player.RPGPlayer;
import me.refluxo.serverlibrary.util.player.PlayerAPI;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import xyz.plocki.asyncthread.AsyncThread;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class StatsBar {

    private static Map<PlayerAPI, BossBar> bars;

    public static void init() {
        bars = new HashMap<>();
        new AsyncThread(() -> bars.keySet().forEach(playerAPI -> {
            RPGPlayer rpgPlayer = new RPGPlayer(playerAPI);
            BossBar tmpBar = bars.get(playerAPI);
            tmpBar.setTitle("§c§l❤ §c" + rpgPlayer.getRPGPlayer().getHealth() + "/" + rpgPlayer.getRPGPlayer().getMaxHealth() + " Leben §a§l| §b§l↯ §b" + rpgPlayer.getRPGPlayer().getPower() + "/" + rpgPlayer.getRPGPlayer().getMaxPower() + " Energie");
            bars.put(playerAPI, tmpBar);
        })).scheduleAsyncTask(0, 10);
    }

    private final PlayerAPI player;

    public StatsBar(PlayerAPI playerAPI) {
        player = playerAPI;
    }

    public void checkPlayer() {
        if(!bars.containsKey(player)) {
            RPGPlayer rpgPlayer = new RPGPlayer(player);
            BossBar newBar = Bukkit.createBossBar("§c§l❤ §c" + rpgPlayer.getRPGPlayer().getHealth() + "/" + rpgPlayer.getRPGPlayer().getMaxHealth() + " Leben §a§l| §b§l↯ §b" + rpgPlayer.getRPGPlayer().getPower() + "/" + rpgPlayer.getRPGPlayer().getMaxPower() + " Energie", BarColor.BLUE, BarStyle.SOLID);
            newBar.addPlayer(player.getAPIPlayer().getBukkitPlayer());
            newBar.setProgress(100);
            bars.put(player, newBar);
        }
    }

    public BossBar getBar() {
        if(!bars.containsKey(player)) {
            RPGPlayer rpgPlayer = new RPGPlayer(player);
            BossBar newBar = Bukkit.createBossBar("§c§l❤ §c" + rpgPlayer.getRPGPlayer().getHealth() + "/" + rpgPlayer.getRPGPlayer().getMaxHealth() + " Leben §a§l| §b§l↯ §b" + rpgPlayer.getRPGPlayer().getPower() + "/" + rpgPlayer.getRPGPlayer().getMaxPower() + " Energie", BarColor.RED, BarStyle.SOLID);
            newBar.addPlayer(player.getAPIPlayer().getBukkitPlayer());
            newBar.setProgress(rpgPlayer.getRPGPlayer().getHealth()* 2L);
            bars.put(player, newBar);
        }
        return bars.get(player);
    }

    public void setBarVisible(boolean visible) {
        checkPlayer();
        BossBar bar = bars.get(player);
        bar.setVisible(visible);
        bars.put(player, bar);
    }

    public void removePlayer() {
        checkPlayer();
        BossBar bar = bars.get(player);
        bar.removePlayer(player.getAPIPlayer().getBukkitPlayer());
        bars.put(player, bar);
    }

    public void addPlayer() {
        checkPlayer();
        BossBar bar = bars.get(player);
        bar.addPlayer(player.getAPIPlayer().getBukkitPlayer());
        bars.put(player, bar);
    }

}
