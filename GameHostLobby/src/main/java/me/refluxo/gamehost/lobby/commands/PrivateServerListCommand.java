package me.refluxo.gamehost.lobby.commands;

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

public class PrivateServerListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Inventory inventory = Bukkit.createInventory(null, 6*9, "§b§lServerliste");
        ItemStack pane = new ItemUtil("§f", Material.BLACK_STAINED_GLASS_PANE, "").buildItem();
        for(int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, pane);
        }
        inventory.setItem(11, new ItemUtil("§bServer Suchen (Spieler Name)", Material.AMETHYST_SHARD, "").buildItem());
        inventory.setItem(15, new ItemUtil("§a§lServerliste", Material.CHEST, "", "", "§cHier kannst du alle Server mit dem Smaragd Paket sehen.", "", "").buildItem());
        ((Player) sender).openInventory(inventory);
        return false;
    }

}
