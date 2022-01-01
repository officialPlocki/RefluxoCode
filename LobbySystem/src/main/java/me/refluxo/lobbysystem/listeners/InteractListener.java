package me.refluxo.lobbysystem.listeners;

import me.refluxo.lobbysystem.util.LocationManager;
import me.refluxo.lobbysystem.util.VisualGUI;
import me.refluxo.serverlibrary.util.inventory.ItemUtil;
import me.refluxo.serverlibrary.util.inventory.PlayerHead;
import me.refluxo.serverlibrary.util.player.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class InteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getItem() == null) return;
        if(event.getItem().getType().equals(Material.PLAYER_HEAD)) {
            if(VisualGUI.containsPlayer(new PlayerManager().getPlayer(event.getPlayer()))) {
                VisualGUI.remove(new PlayerManager().getPlayer(event.getPlayer()));
                event.getPlayer().getInventory().setItem(4, new ItemUtil("§a§lNavigator", PlayerHead.getItemStackWithTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmYyZjg3MTkzNmM2NWFlZjMxZDcxNWNiMTg5MjcxZWY5MTEzYWEyZWVhMWQ3MDYzYTQ3YzU1YzQ2OTJmMjIzIn19fQ=="), "§7Öffne das Menü").buildItem());
            } else {
                VisualGUI.add(new PlayerManager().getPlayer(event.getPlayer()));
                event.getPlayer().getInventory().clear();
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if(!event.getEntityType().equals(EntityType.ARMOR_STAND)) {
            event.setCancelled(true);
            if(!event.getEntity().isDead()) {
                event.getEntity().remove();
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event){
        event.setCancelled(true);
        if(event.getDamager() instanceof Player) {
            if(event.getEntity() instanceof ArmorStand) {
                if(Objects.requireNonNull(event.getEntity().getCustomName()).equalsIgnoreCase(event.getDamager().getName() + "-left")) {
                    VisualGUI.remove(new PlayerManager().getPlayer((Player) event.getDamager()));
                    event.getDamager().teleport(new LocationManager().getLocation("explore"));
                    ((Player) event.getDamager()).getInventory().setItem(4, new ItemUtil("§a§lNavigator", PlayerHead.getItemStackWithTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmYyZjg3MTkzNmM2NWFlZjMxZDcxNWNiMTg5MjcxZWY5MTEzYWEyZWVhMWQ3MDYzYTQ3YzU1YzQ2OTJmMjIzIn19fQ=="), "§7Öffne das Menü").buildItem());
                } else if(event.getEntity().getCustomName().equalsIgnoreCase(event.getDamager().getName() + "-right")) {
                    VisualGUI.remove(new PlayerManager().getPlayer((Player) event.getDamager()));
                    event.getDamager().teleport(new LocationManager().getLocation("game-host"));
                    ((Player) event.getDamager()).getInventory().setItem(4, new ItemUtil("§a§lNavigator", PlayerHead.getItemStackWithTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmYyZjg3MTkzNmM2NWFlZjMxZDcxNWNiMTg5MjcxZWY5MTEzYWEyZWVhMWQ3MDYzYTQ3YzU1YzQ2OTJmMjIzIn19fQ=="), "§7Öffne das Menü").buildItem());
                }
            }
        }
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        event.setCancelled(true);
        if(Objects.requireNonNull(event.getRightClicked().getCustomName()).equalsIgnoreCase(event.getPlayer().getName() + "-left")) {
            VisualGUI.remove(new PlayerManager().getPlayer(event.getPlayer()));
            event.getPlayer().teleport(new LocationManager().getLocation("explore"));
            event.getPlayer().getInventory().setItem(4, new ItemUtil("§a§lNavigator", PlayerHead.getItemStackWithTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmYyZjg3MTkzNmM2NWFlZjMxZDcxNWNiMTg5MjcxZWY5MTEzYWEyZWVhMWQ3MDYzYTQ3YzU1YzQ2OTJmMjIzIn19fQ=="), "§7Öffne das Menü").buildItem());
        } else if(event.getRightClicked().getCustomName().equalsIgnoreCase(event.getPlayer().getName() + "-right")) {
            VisualGUI.remove(new PlayerManager().getPlayer(event.getPlayer()));
            event.getPlayer().teleport(new LocationManager().getLocation("game-host"));
            event.getPlayer().getInventory().setItem(4, new ItemUtil("§a§lNavigator", PlayerHead.getItemStackWithTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmYyZjg3MTkzNmM2NWFlZjMxZDcxNWNiMTg5MjcxZWY5MTEzYWEyZWVhMWQ3MDYzYTQ3YzU1YzQ2OTJmMjIzIn19fQ=="), "§7Öffne das Menü").buildItem());
        }
    }

}
