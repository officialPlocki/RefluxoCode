package me.refluxo.gamehost.lobby.commands;

import me.refluxo.gamehost.lobby.listeners.InventoryClickListener;
import me.refluxo.gamehost.lobby.util.PrivateServerConfigurationMySQL;
import me.refluxo.gamehost.lobby.util.PrivateServerMySQL;
import me.refluxo.serverlibrary.ServerLibrary;
import me.refluxo.serverlibrary.util.inventory.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PrivateServerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!new PrivateServerMySQL((Player) sender).getInstanceName().equalsIgnoreCase("")) {
            if(new PrivateServerConfigurationMySQL(new PrivateServerMySQL(((Player) sender).getPlayer()).getInstanceUUID()).needSetup()) {
                new PrivateServerConfigurationMySQL(new PrivateServerMySQL(((Player) sender).getPlayer()).getInstanceUUID()).setOwnerUUID(((Player) sender).getUniqueId().toString());
            }
            Inventory inventory4 = Bukkit.createInventory(null, 5*9, "§dWas möchtest du Spielen?");
            ItemStack pane = new ItemUtil("§f", Material.BLACK_STAINED_GLASS_PANE, "").buildItem();
            for(int i = 0; i < inventory4.getSize(); i++) {
                inventory4.setItem(i, pane);
            }
            inventory4.setItem(11, new ItemUtil("§a§lVorlagen", Material.PAPER, "", "§bWähle einen Server aus Vorlagen aus und passe ihn\n", "nach deinen Wünschen an.\n", "").buildItem());
            inventory4.setItem(15, new ItemUtil("§b§lCustom", Material.CHEST, "", "§bErstelle deinen eigenen Server, ganz ohne Vorlagen.\n", "").buildItem());
            Bukkit.getScheduler().scheduleSyncDelayedTask(ServerLibrary.getPlugin(), () -> {
                ((Player) sender).openInventory(inventory4);
            }, 3);
        } else {
            Inventory inventory = Bukkit.createInventory(null, 3*9, "§dMöchtest du deinen Server zurücksetzen?");
            ItemStack pane = new ItemUtil("§f", Material.BLACK_STAINED_GLASS_PANE, "").buildItem();
            for(int i = 0; i < inventory.getSize(); i++) {
               inventory.setItem(i, pane);
            }
            inventory.setItem(11, new ItemUtil("§a§lJa", Material.LIME_DYE, "", "", "§cAchtung: Dies löscht deinen Server unwiederruflich!\n", "", "").buildItem());
            inventory.setItem(15, new ItemUtil("§c§lNein", Material.RED_DYE, "", "", "§aGehe zu dein Einstellungen.\n", "", "").buildItem());
            ((Player) sender).openInventory(inventory);
        }
        return false;
    }

}
