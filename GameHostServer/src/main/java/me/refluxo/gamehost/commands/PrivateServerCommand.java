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
        if(playerAPI.hasPermission("ps.owner")) {
            Inventory inventory = Bukkit.createInventory(null, 3*9, "§c§lGameHost §8» §a§lOptionen");
            ItemStack pane = new ItemUtil("", Material.BLACK_STAINED_GLASS_PANE, "").buildItem();
            for(int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, pane);
            }
            playerAPI.getAPIPlayer().getBukkitPlayer().openInventory(inventory);
        } else {
            playerAPI.sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.CHAT, "Du hast dazu keine Rechte.");
        }
        return false;
    }

}
