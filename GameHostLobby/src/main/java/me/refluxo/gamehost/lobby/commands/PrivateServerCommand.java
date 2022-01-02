package me.refluxo.gamehost.lobby.commands;

import me.refluxo.gamehost.lobby.listeners.InventoryClickListener;
import me.refluxo.gamehost.lobby.util.PrivateServerConfigurationMySQL;
import me.refluxo.gamehost.lobby.util.PrivateServerMySQL;
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
        if(new PrivateServerConfigurationMySQL(new PrivateServerMySQL(((Player) sender).getPlayer()).getInstanceUUID()).needSetup()) {
            new PrivateServerConfigurationMySQL(new PrivateServerMySQL(((Player) sender).getPlayer()).getInstanceUUID()).setOwnerUUID(((Player) sender).getUniqueId().toString());
        }
        if(!new PrivateServerMySQL((Player) sender).getInstanceName().equalsIgnoreCase("")) {
            ((Player) sender).openInventory(Objects.requireNonNull(InventoryClickListener.getInventory(InventoryClickListener.inventoryType.CHOOSE, (Player) sender)));
        } else {
            Inventory inventory = Bukkit.createInventory(null, 3*9, "§dMöchtest du deinen Server zurücksetzen?");
            ItemStack pane = new ItemUtil("", Material.BLACK_STAINED_GLASS_PANE, "").buildItem();
            for(int i = 0; i < inventory.getSize(); i++) {
               inventory.setItem(i, pane);
            }
            inventory.setItem(11, new ItemUtil("§a§lJa", Material.LIME_DYE, "\n\n§cAchtung: Dies löscht deinen Server unwiederruflich!\n\n").buildItem());
            inventory.setItem(15, new ItemUtil("§c§lNein", Material.RED_DYE, "\n\n§aGehe zu dein Einstellungen.\n\n").buildItem());
            ((Player) sender).openInventory(inventory);
        }
        return false;
    }

}
