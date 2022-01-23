package me.refluxo.gamehost;

import me.refluxo.gamehost.commands.PrivateServerCommand;
import me.refluxo.gamehost.listeners.JoinListener;
import me.refluxo.gamehost.util.PrivateServerMySQL;
import me.refluxo.gamehost.util.ServerInformationManager;
import me.refluxo.serverlibrary.util.command.CommandBuilder;
import me.refluxo.serverlibrary.util.player.PlayerAPI;
import me.refluxo.serverlibrary.util.player.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

public final class Gamehost extends JavaPlugin {

    private static boolean b = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        ServerInformationManager.init();
        Objects.requireNonNull(this.getCommand("ps")).setExecutor(new PrivateServerCommand());
        new CommandBuilder(new Command("rl", "", "/reload", List.of("reload", "bukkit:rl", "bukkit:reload")) {
            @Override
            public boolean execute(CommandSender sender, String label, String[] args) {
                new PlayerManager().getPlayer((Player) sender).sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.CHAT, "GameHost Server können nicht neugeladen werden.");
                return false;
            }
        }).register();
        new CommandBuilder(new Command("stop", "Stoppt den Server", "/stop", List.of("minecraft:stop", "bukkit:stop")) {
            @Override
            public boolean execute(CommandSender sender, String label, String[] args) {
                if(sender.hasPermission("ps.stop")) {
                    new PrivateServerMySQL(Bukkit.getOfflinePlayer(new ServerInformationManager().getOwnerUUID()).getPlayer()).setInstanceOnline(false);
                    Bukkit.getServer().shutdown();
                } else {
                    new PlayerManager().getPlayer((Player) sender).sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.CHAT, "Du hast dazu keine Rechte.");
                }
                return false;
            }
        }).register();
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if(b) {
                new PrivateServerMySQL(Bukkit.getOfflinePlayer(new ServerInformationManager().getOwnerUUID()).getPlayer()).setInstanceOnline(false);
                Bukkit.getServer().shutdown();
            }
            if(Bukkit.getServer().getOnlinePlayers().size() == 0) {
                b = true;
            }
        }, 20*60, 20*5);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
