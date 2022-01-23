package me.refluxo.gamehost.commands;

import me.refluxo.serverlibrary.util.inventory.ItemUtil;
import me.refluxo.serverlibrary.util.player.PlayerAPI;
import me.refluxo.serverlibrary.util.player.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PrivateServerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PlayerAPI playerAPI = new PlayerManager().getPlayer((Player) sender);
        if(playerAPI.hasPermission("ps.settings")) {
            Inventory inventory = Bukkit.createInventory(null, 3*9, "§c§lGameHost §8» §a§lOptionen");
            ItemStack pane = new ItemUtil("§f", Material.BLACK_STAINED_GLASS_PANE, "").buildItem();
            for(int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, pane);
            }
            inventory.setItem(11, new ItemUtil("§a§lPlugin Installieren", Material.CHEST, "", "§bInstalliere ein Plugin von SpigotMC", "§aBitte halte die max. 6 stellige ID bereit.", "").buildItem());
            inventory.setItem(13, new ItemUtil("§c§lServer Stoppen", Material.RED_DYE, "").buildItem());
            ((Player) sender).openInventory(inventory);
        } else {
            playerAPI.sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.CHAT, "Du hast dazu keine Rechte.");
        }
        return false;
    }

}
