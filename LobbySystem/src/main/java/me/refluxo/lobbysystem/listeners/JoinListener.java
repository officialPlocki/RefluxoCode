package me.refluxo.lobbysystem.listeners;

import me.refluxo.lobbysystem.util.LocationManager;
import me.refluxo.serverlibrary.util.inventory.ItemUtil;
import me.refluxo.serverlibrary.util.inventory.PlayerHead;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(new LocationManager().getLocation("spawn"));
        event.getPlayer().getInventory().clear();
        event.getPlayer().getInventory().setItem(4, new ItemUtil("§a§lNavigator", PlayerHead.getItemStackWithTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmYyZjg3MTkzNmM2NWFlZjMxZDcxNWNiMTg5MjcxZWY5MTEzYWEyZWVhMWQ3MDYzYTQ3YzU1YzQ2OTJmMjIzIn19fQ=="), "§7Öffne das Menü").buildItem());
    }

}
