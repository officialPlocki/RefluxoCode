package me.refluxo.lobbysystem.util;

import me.refluxo.serverlibrary.util.inventory.PlayerHead;
import me.refluxo.serverlibrary.util.player.PlayerAPI;
import me.refluxo.serverlibrary.util.player.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.plocki.asyncthread.AsyncThread;

import java.util.*;

public class VisualGUI implements Listener {

    public static List<Player> players;
    private static Map<Player, ArmorStand> firstEntity;
    private static Map<Player, ArmorStand> secondEntity;
    private static Map<Player, Integer> openTime;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(players.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    public static boolean containsPlayer(PlayerAPI playerAPI) {
        return players.contains(playerAPI.getAPIPlayer().getBukkitPlayer());
    }

    public static void init() {
        players = new ArrayList<>();
        firstEntity = new HashMap<>();
        secondEntity = new HashMap<>();
        openTime = new HashMap<>();
        new AsyncThread(() -> players.forEach(player -> {
            openTime.put(player, openTime.getOrDefault(player, 0)+1);
            if(openTime.getOrDefault(player, 0) >= 120) {
                remove(new PlayerManager().getPlayer(player));
            }
        })).scheduleAsyncTask(0,1);
    }

    public static void add(PlayerAPI playerAPI) {
        Player player = playerAPI.getAPIPlayer().getBukkitPlayer();
        player.sendMessage("4");
        players.add(player);
        Entity leftPassenger = player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        ArmorStand leftPassengerArmorStand = (ArmorStand) leftPassenger;
        leftPassengerArmorStand.setCustomName("§a§lExplore");
        leftPassengerArmorStand.setVisible(false);
        leftPassengerArmorStand.setGravity(false);
        leftPassengerArmorStand.setInvulnerable(true);
        leftPassengerArmorStand.setSmall(true);
        leftPassengerArmorStand.setCustomNameVisible(true);
        Entity rightPassenger = player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        ArmorStand rightPassengerArmorStand = (ArmorStand)rightPassenger;
        rightPassengerArmorStand.setCustomName("§b§lGame-Host");
        rightPassengerArmorStand.setVisible(false);
        rightPassengerArmorStand.setGravity(false);
        rightPassengerArmorStand.setInvulnerable(true);
        rightPassengerArmorStand.setSmall(true);
        rightPassengerArmorStand.setCustomNameVisible(true);
        Location leftLocation = player.getLocation().clone().subtract(1.2,0.8,0).add(0,0,0.8);
        Location rightLocation = player.getLocation().clone().subtract(1.2,0.8,0.8);
        Entity left = player.getLocation().getWorld().spawnEntity(leftLocation, EntityType.ARMOR_STAND);
        Entity right = player.getLocation().getWorld().spawnEntity(rightLocation, EntityType.ARMOR_STAND);
        ArmorStand leftArmorStand = (ArmorStand) left;
        ArmorStand rightArmorStand = (ArmorStand) right;
        leftArmorStand.addPassenger(leftPassengerArmorStand);
        rightArmorStand.addPassenger(rightPassengerArmorStand);
        leftArmorStand.setVisible(false);
        rightArmorStand.setVisible(false);
        leftArmorStand.setInvulnerable(true);
        rightArmorStand.setInvulnerable(true);
        leftArmorStand.setGravity(false);
        rightArmorStand.setGravity(false);
        leftArmorStand.setCustomName(player.getName() + "-left");
        leftArmorStand.setCustomNameVisible(false);
        rightArmorStand.setCustomName(player.getName() + "-right");
        rightArmorStand.setCustomNameVisible(false);
        leftArmorStand.getEquipment().setHelmet(PlayerHead.getItemStackWithTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmUyY2M0MjAxNWU2Njc4ZjhmZDQ5Y2NjMDFmYmY3ODdmMWJhMmMzMmJjZjU1OWEwMTUzMzJmYzVkYjUwIn19fQ=="));
        rightArmorStand.getEquipment().setHelmet(PlayerHead.getItemStackWithTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNjY2NkNjQ2OWI4NjExZThjNmI4MzNlMmU3YjE0M2JkMzBhOGEzNTdmN2ZiY2Q0YTU0ZjJjMGQzYmRhNjQyYyJ9fX0="));
        firstEntity.put(player, leftArmorStand);
        secondEntity.put(player, rightArmorStand);
    }

    public static void remove(PlayerAPI playerAPI) {
        Player player = playerAPI.getAPIPlayer().getBukkitPlayer();
        player.sendMessage("4");
        players.remove(player);
        ArmorStand leftArmorStand = firstEntity.get(player);
        ArmorStand rightArmorStand = secondEntity.get(player);
        leftArmorStand.getPassenger().remove();
        rightArmorStand.getPassenger().remove();
        leftArmorStand.remove();
        rightArmorStand.remove();
        firstEntity.remove(player);
        secondEntity.remove(player);
    }

}
