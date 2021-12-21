package me.refluxo.rpglibrary.commands;

import me.refluxo.serverlibrary.util.command.CommandBuilder;
import me.refluxo.serverlibrary.util.inventory.InventoryBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SkillsCommand {

    private final CommandBuilder command;

    public SkillsCommand() {
        command = new CommandBuilder(new Command("skills", "Öffnet das Skill Menü", "/skills", new ArrayList<>()) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                if(!(sender instanceof Player)) {
                    return false;
                }
                Map<Integer, ItemStack> items = new HashMap<>();



                Inventory inventory = new InventoryBuilder("§a§lSkills", 9*3).buildInventory(items);
                ((Player)sender).openInventory(inventory);
                return false;
            }
        });
    }

    public void register() {
        command.register();
    }

    public void unregister() {
        command.unregister();
    }

}
