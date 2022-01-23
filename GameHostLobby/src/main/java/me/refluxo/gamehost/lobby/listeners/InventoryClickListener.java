package me.refluxo.gamehost.lobby.listeners;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.driver.service.ServiceTemplate;
import me.refluxo.gamehost.lobby.util.PrivateServerConfigurationMySQL;
import me.refluxo.gamehost.lobby.util.PrivateServerManager;
import me.refluxo.gamehost.lobby.util.PrivateServerMySQL;
import me.refluxo.serverlibrary.ServerLibrary;
import me.refluxo.serverlibrary.util.cloud.BungeeCord;
import me.refluxo.serverlibrary.util.inventory.ItemUtil;
import me.refluxo.serverlibrary.util.player.PlayerAPI;
import me.refluxo.serverlibrary.util.player.PlayerManager;
import me.refluxo.serverlibrary.util.sql.MySQLService;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.plocki.asyncthread.AsyncThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InventoryClickListener implements Listener {

    private static final Map<Player, Boolean> motdSet = new HashMap<>();
    private static final Map<Player, Boolean> search = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) throws IOException {
        if(motdSet.getOrDefault(event.getPlayer(), false)) {
            event.setCancelled(true);
            if(event.getMessage().length() <= 30) {
                new PrivateServerConfigurationMySQL(new PrivateServerMySQL(event.getPlayer()).getInstanceUUID()).setMOTD(event.getMessage().replaceAll("&", "§"));
                new PlayerManager().getPlayer(event.getPlayer()).sendMessage(PlayerAPI.MessageType.NORMAL, ChatMessageType.CHAT, "Du hast die MOTD geändert.");
                motdSet.put(event.getPlayer(), false);
            } else {
                new PlayerManager().getPlayer(event.getPlayer()).sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.CHAT, "Die MOTD darf nicht länger als 30 Zeichen sein.");
            }
        }
        if(search.getOrDefault(event.getPlayer(), false)) {
            event.setCancelled(true);
            search.put(event.getPlayer(), false);
            String playerUUID = getPlayerUUIDFromName(event.getMessage());;
            String instance = getInstanceNameFromPlayerUUID(playerUUID);
            if(instance != null) {
                new BungeeCord().sendPlayer(event.getPlayer(), instance);
            } else {
                new PlayerManager().getPlayer(event.getPlayer()).sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.CHAT, "Dieser Spieler hat keinen Server!");
                Inventory inventory = Bukkit.createInventory(null, 6*9, "§a§lSmaragd-Paket Serverliste");
                ItemStack pane = new ItemUtil("§f", Material.BLACK_STAINED_GLASS_PANE, "").buildItem();
                for(int i = 0; i < inventory.getSize(); i++) {
                    inventory.setItem(i, pane);
                }
                List<ServiceInfoSnapshot> snapshots = new PrivateServerManager(new PlayerManager().getPlayer(event.getPlayer())).getAllInstances();
                for(int i = 0; i < (inventory.getSize()-9); i++) {
                    if(snapshots.get(i) != null) {
                        inventory.setItem(i, new ItemUtil(snapshots.get(i).getName(), Material.GREEN_STAINED_GLASS_PANE, "", "§f\"" + new PrivateServerConfigurationMySQL(getInstanceUUID(snapshots.get(i).getName())).getMOTD() + "\"", "", "§aDieser Server gehört: §b" + getPlayerNameFromUUID(getInstanceOwnerUUID(getInstanceUUID(snapshots.get(i).getName()))) + "", "").buildItem());
                    }
                }
                event.getPlayer().openInventory(inventory);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) throws IOException {
        if(event.getCurrentItem() != null) {
            event.setCancelled(true);
            String title = event.getView().getTitle();
            ItemStack item = event.getCurrentItem();
            ItemStack pane = new ItemUtil("§f", Material.BLACK_STAINED_GLASS_PANE, "").buildItem();
            String displayName = Objects.requireNonNull(item.getItemMeta()).getDisplayName();
            PlayerAPI playerAPI = new PlayerManager().getPlayer((Player) event.getWhoClicked());
            PrivateServerMySQL server = new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer());
            PrivateServerConfigurationMySQL configuration = new PrivateServerConfigurationMySQL(server.getInstanceUUID());
            if(title.equalsIgnoreCase("§dMöchtest du deinen Server zurücksetzen?")) {
                if(displayName.equalsIgnoreCase("§a§lJa")) {
                    playerAPI.sendMessage(PlayerAPI.MessageType.WARNING, ChatMessageType.ACTION_BAR,"Dein Server wird nun gelöscht...");
                    event.getWhoClicked().closeInventory();
                    ServiceTemplate template = new ServiceTemplate("ps", new PrivateServerMySQL(((Player) event.getWhoClicked())).getInstanceUUID(), "saves");
                    template.storage().delete();
                    template.storage().create();
                    Inventory inventory4 = Bukkit.createInventory(null, 5*9, "§dWas möchtest du Spielen?");
                    for(int i = 0; i < inventory4.getSize(); i++) {
                        inventory4.setItem(i, pane);
                    }
                    inventory4.setItem(11, new ItemUtil("§a§lVorlagen", Material.PAPER, "", "§bWähle einen Server aus Vorlagen aus und passe ihn\n", "nach deinen Wünschen an.\n", "").buildItem());
                    inventory4.setItem(15, new ItemUtil("§b§lCustom", Material.CHEST, "", "§bErstelle deinen eigenen Server, ganz ohne Vorlagen.\n", "").buildItem());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(ServerLibrary.getPlugin(), () -> event.getWhoClicked().openInventory(inventory4), 3);
                } else if(displayName.equalsIgnoreCase("§c§lNein")) {
                    event.getWhoClicked().closeInventory();
                    Inventory inventory2 = Bukkit.createInventory(null, 6*9, "§6§lEinstellungen");
                    for(int i = 0; i < inventory2.getSize(); i++) {
                        inventory2.setItem(i, pane);
                    }
                    inventory2.setItem(20, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                    inventory2.setItem(21, new ItemUtil("§6§lMOTD", Material.BOOK, "", "§aÄndere die MOTD\n", "", "Aktuelle MOTD: " + new PrivateServerConfigurationMySQL(new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceUUID()).getMOTD() + "", "").buildItem());
                    String string;
                    boolean maintenance = new PrivateServerConfigurationMySQL(new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceUUID()).isInMaintenance();
                    if(maintenance) {
                        string = "§a§lJa";
                    } else {
                        string = "§c§lNein";
                    }
                    inventory2.setItem(22, new ItemUtil("§c§lWartungen", Material.REPEATER, "", "§aSetze den Server in den Wartungsmodus\n", "", "§bAktiviert: §e" + string).buildItem());
                    inventory2.setItem(23, new ItemUtil("§b§lHosting Pakete", Material.BEACON, "", "§aMiete ein Hosting Paket von Coins\n", "").buildItem());
                    inventory2.setItem(24, new ItemUtil("§c", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                    inventory2.setItem(29, new ItemUtil("§c", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                    inventory2.setItem(30, new ItemUtil("§c", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                    inventory2.setItem(31, new ItemUtil("§c", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                    inventory2.setItem(32, new ItemUtil("§c", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                    inventory2.setItem(33, new ItemUtil("§c", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                    if(new PrivateServerMySQL((Player) event.getWhoClicked()).isInstanceOnline()) {
                        inventory2.setItem(53, new ItemUtil("§c§lServer Stoppen", Material.RED_DYE, "").buildItem());
                        inventory2.setItem(8, new ItemUtil("§b§lVerbinden", Material.ENDER_PEARL, "").buildItem());
                    } else {
                        inventory2.setItem(53, new ItemUtil("§a§lServer Starten", Material.LIME_DYE, "").buildItem());
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(ServerLibrary.getPlugin(), () -> event.getWhoClicked().openInventory(inventory2), 3);
                }
            } else if(title.equalsIgnoreCase("§b§lHosting Pakete")) {
                if(displayName.equalsIgnoreCase("§a§l4096mb")) {
                    if(playerAPI.hasCoins(80)) {
                        configuration.setServerRAM(4096);
                        configuration.resetPackageTime();
                        playerAPI.removeCoins(80);
                        playerAPI.sendMessage(PlayerAPI.MessageType.NORMAL, ChatMessageType.CHAT, "Du hast das Smaragd Hosting Paket für 24h Serverlaufzeit gebucht.");
                        event.getWhoClicked().closeInventory();
                        Inventory inventory2 = Bukkit.createInventory(null, 6*9, "§6§lEinstellungen");
                        for(int i = 0; i < inventory2.getSize(); i++) {
                            inventory2.setItem(i, pane);
                        }
                        inventory2.setItem(20, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(21, new ItemUtil("§6§lMOTD", Material.BOOK, "", "§aÄndere die MOTD\n", "", "Aktuelle MOTD: " + new PrivateServerConfigurationMySQL(new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceUUID()).getMOTD() + "", "").buildItem());
                        String string;
                        boolean maintenance = new PrivateServerConfigurationMySQL(new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceUUID()).isInMaintenance();
                        if(maintenance) {
                            string = "§a§lJa";
                        } else {
                            string = "§c§lNein";
                        }
                        inventory2.setItem(22, new ItemUtil("§c§lWartungen", Material.REPEATER, "", "§aSetze den Server in den Wartungsmodus\n", "", "§bAktiviert: §e" + string).buildItem());
                        inventory2.setItem(23, new ItemUtil("§b§lHosting Pakete", Material.BEACON, "", "§aMiete ein Hosting Paket von Coins\n", "").buildItem());
                        inventory2.setItem(24, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(29, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(30, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(31, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(32, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(33, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        if(new PrivateServerMySQL((Player) event.getWhoClicked()).isInstanceOnline()) {
                            inventory2.setItem(53, new ItemUtil("§c§lServer Stoppen", Material.RED_DYE, "").buildItem());
                            inventory2.setItem(8, new ItemUtil("§b§lVerbinden", Material.ENDER_PEARL, "").buildItem());
                        } else {
                            inventory2.setItem(53, new ItemUtil("§a§lServer Starten", Material.LIME_DYE, "").buildItem());
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(ServerLibrary.getPlugin(), () -> event.getWhoClicked().openInventory(inventory2), 3);
                    } else {
                        playerAPI.sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.CHAT, "Du hast nicht genügend Coins.");
                    }
                } else if(displayName.equalsIgnoreCase("§b§l3072mb")) {
                    if(playerAPI.hasCoins(60)) {
                        configuration.setServerRAM(3072);
                        configuration.resetPackageTime();
                        playerAPI.removeCoins(60);
                        playerAPI.sendMessage(PlayerAPI.MessageType.NORMAL, ChatMessageType.CHAT, "Du hast das Diamant Hosting Paket für 24h Serverlaufzeit gebucht.");
                        event.getWhoClicked().closeInventory();
                        Inventory inventory2 = Bukkit.createInventory(null, 6*9, "§6§lEinstellungen");
                        for(int i = 0; i < inventory2.getSize(); i++) {
                            inventory2.setItem(i, pane);
                        }
                        inventory2.setItem(20, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(21, new ItemUtil("§6§lMOTD", Material.BOOK, "", "§aÄndere die MOTD\n", "", "Aktuelle MOTD: " + new PrivateServerConfigurationMySQL(new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceUUID()).getMOTD() + "", "").buildItem());
                        String string;
                        boolean maintenance = new PrivateServerConfigurationMySQL(new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceUUID()).isInMaintenance();
                        if(maintenance) {
                            string = "§a§lJa";
                        } else {
                            string = "§c§lNein";
                        }
                        inventory2.setItem(22, new ItemUtil("§c§lWartungen", Material.REPEATER, "", "§aSetze den Server in den Wartungsmodus\n", "", "§bAktiviert: §e" + string).buildItem());
                        inventory2.setItem(23, new ItemUtil("§b§lHosting Pakete", Material.BEACON, "", "§aMiete ein Hosting Paket von Coins\n", "").buildItem());
                        inventory2.setItem(24, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(29, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(30, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(31, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(32, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(33, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        if(new PrivateServerMySQL((Player) event.getWhoClicked()).isInstanceOnline()) {
                            inventory2.setItem(53, new ItemUtil("§c§lServer Stoppen", Material.RED_DYE, "").buildItem());
                            inventory2.setItem(8, new ItemUtil("§b§lVerbinden", Material.ENDER_PEARL, "").buildItem());
                        } else {
                            inventory2.setItem(53, new ItemUtil("§a§lServer Starten", Material.LIME_DYE, "").buildItem());
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(ServerLibrary.getPlugin(), () -> event.getWhoClicked().openInventory(inventory2), 3);
                    } else {
                        playerAPI.sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.CHAT, "Du hast nicht genügend Coins.");
                    }
                } else if(displayName.equalsIgnoreCase("§6§l2048mb")) {
                    if(playerAPI.hasCoins(40)) {
                        configuration.setServerRAM(2048);
                        configuration.resetPackageTime();
                        playerAPI.removeCoins(40);
                        playerAPI.sendMessage(PlayerAPI.MessageType.NORMAL, ChatMessageType.CHAT, "Du hast das Gold Hosting Paket für 24h Serverlaufzeit gebucht.");
                        event.getWhoClicked().closeInventory();
                        Inventory inventory2 = Bukkit.createInventory(null, 6*9, "§6§lEinstellungen");
                        for(int i = 0; i < inventory2.getSize(); i++) {
                            inventory2.setItem(i, pane);
                        }
                        inventory2.setItem(20, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(21, new ItemUtil("§6§lMOTD", Material.BOOK, "", "§aÄndere die MOTD\n", "", "Aktuelle MOTD: " + new PrivateServerConfigurationMySQL(new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceUUID()).getMOTD() + "", "").buildItem());
                        String string;
                        boolean maintenance = new PrivateServerConfigurationMySQL(new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceUUID()).isInMaintenance();
                        if(maintenance) {
                            string = "§a§lJa";
                        } else {
                            string = "§c§lNein";
                        }
                        inventory2.setItem(22, new ItemUtil("§c§lWartungen", Material.REPEATER, "", "§aSetze den Server in den Wartungsmodus\n", "", "§bAktiviert: §e" + string).buildItem());
                        inventory2.setItem(23, new ItemUtil("§b§lHosting Pakete", Material.BEACON, "", "§aMiete ein Hosting Paket von Coins\n", "").buildItem());
                        inventory2.setItem(24, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(29, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(30, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(31, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(32, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(33, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        if(new PrivateServerMySQL((Player) event.getWhoClicked()).isInstanceOnline()) {
                            inventory2.setItem(53, new ItemUtil("§c§lServer Stoppen", Material.RED_DYE, "").buildItem());
                            inventory2.setItem(8, new ItemUtil("§b§lVerbinden", Material.ENDER_PEARL, "").buildItem());
                        } else {
                            inventory2.setItem(53, new ItemUtil("§a§lServer Starten", Material.LIME_DYE, "").buildItem());
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(ServerLibrary.getPlugin(), () -> event.getWhoClicked().openInventory(inventory2), 3);
                    } else {
                        playerAPI.sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.CHAT, "Du hast nicht genügend Coins.");
                    }
                } else if(displayName.equalsIgnoreCase("§f§l1536mb")) {
                    if(playerAPI.hasCoins(30)) {
                        configuration.setServerRAM(1536);
                        configuration.resetPackageTime();
                        playerAPI.removeCoins(30);
                        playerAPI.sendMessage(PlayerAPI.MessageType.NORMAL, ChatMessageType.CHAT, "Du hast das Eisen Hosting Paket für 24h Serverlaufzeit gebucht.");
                        event.getWhoClicked().closeInventory();
                        Inventory inventory2 = Bukkit.createInventory(null, 6*9, "§6§lEinstellungen");
                        for(int i = 0; i < inventory2.getSize(); i++) {
                            inventory2.setItem(i, pane);
                        }
                        inventory2.setItem(20, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(21, new ItemUtil("§6§lMOTD", Material.BOOK, "", "§aÄndere die MOTD\n", "", "Aktuelle MOTD: " + new PrivateServerConfigurationMySQL(new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceUUID()).getMOTD() + "", "").buildItem());
                        String string;
                        boolean maintenance = new PrivateServerConfigurationMySQL(new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceUUID()).isInMaintenance();
                        if(maintenance) {
                            string = "§a§lJa";
                        } else {
                            string = "§c§lNein";
                        }
                        inventory2.setItem(22, new ItemUtil("§c§lWartungen", Material.REPEATER, "", "§aSetze den Server in den Wartungsmodus\n", "", "§bAktiviert: §e" + string).buildItem());
                        inventory2.setItem(23, new ItemUtil("§b§lHosting Pakete", Material.BEACON, "", "§aMiete ein Hosting Paket von Coins\n", "").buildItem());
                        inventory2.setItem(24, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(29, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(30, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(31, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(32, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(33, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        if(new PrivateServerMySQL((Player) event.getWhoClicked()).isInstanceOnline()) {
                            inventory2.setItem(53, new ItemUtil("§c§lServer Stoppen", Material.RED_DYE, "").buildItem());
                            inventory2.setItem(8, new ItemUtil("§b§lVerbinden", Material.ENDER_PEARL, "").buildItem());
                        } else {
                            inventory2.setItem(53, new ItemUtil("§a§lServer Starten", Material.LIME_DYE, "").buildItem());
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(ServerLibrary.getPlugin(), () -> event.getWhoClicked().openInventory(inventory2), 3);
                    } else {
                        playerAPI.sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.CHAT, "Du hast nicht genügend Coins.");
                    }
                } else if(displayName.equalsIgnoreCase("§7§l1024mb")) {
                    if(playerAPI.hasCoins(20)) {
                        configuration.setServerRAM(1024);
                        configuration.resetPackageTime();
                        playerAPI.removeCoins(20);
                        playerAPI.sendMessage(PlayerAPI.MessageType.NORMAL, ChatMessageType.CHAT, "Du hast das Bronze Hosting Paket für 24h Serverlaufzeit gebucht.");
                        event.getWhoClicked().closeInventory();
                        Inventory inventory2 = Bukkit.createInventory(null, 6*9, "§6§lEinstellungen");
                        for(int i = 0; i < inventory2.getSize(); i++) {
                            inventory2.setItem(i, pane);
                        }
                        inventory2.setItem(20, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(21, new ItemUtil("§6§lMOTD", Material.BOOK, "", "§aÄndere die MOTD\n", "", "Aktuelle MOTD: " + new PrivateServerConfigurationMySQL(new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceUUID()).getMOTD() + "", "").buildItem());
                        String string;
                        boolean maintenance = new PrivateServerConfigurationMySQL(new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceUUID()).isInMaintenance();
                        if(maintenance) {
                            string = "§a§lJa";
                        } else {
                            string = "§c§lNein";
                        }
                        inventory2.setItem(22, new ItemUtil("§c§lWartungen", Material.REPEATER, "", "§aSetze den Server in den Wartungsmodus\n", "", "§bAktiviert: §e" + string).buildItem());
                        inventory2.setItem(23, new ItemUtil("§b§lHosting Pakete", Material.BEACON, "", "§aMiete ein Hosting Paket von Coins\n", "").buildItem());
                        inventory2.setItem(24, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(29, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(30, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(31, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(32, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        inventory2.setItem(33, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                        if(new PrivateServerMySQL((Player) event.getWhoClicked()).isInstanceOnline()) {
                            inventory2.setItem(53, new ItemUtil("§c§lServer Stoppen", Material.RED_DYE, "").buildItem());
                            inventory2.setItem(8, new ItemUtil("§b§lVerbinden", Material.ENDER_PEARL, "").buildItem());
                        } else {
                            inventory2.setItem(53, new ItemUtil("§a§lServer Starten", Material.LIME_DYE, "").buildItem());
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(ServerLibrary.getPlugin(), () -> event.getWhoClicked().openInventory(inventory2), 3);
                    } else {
                        playerAPI.sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.CHAT, "Du hast nicht genügend Coins.");
                    }
                }
            } else if(title.equalsIgnoreCase("§6§lEinstellungen")) {
                if(displayName.equalsIgnoreCase("§6§lMOTD")) {
                    event.getWhoClicked().closeInventory();
                    motdSet.put((Player) event.getWhoClicked(), true);
                    playerAPI.sendMessage(PlayerAPI.MessageType.NORMAL, ChatMessageType.CHAT, "Schreibe jetzt die MOTD in den Chat, die du setzen möchtest. Verwende \"&\" zum verwenden von Farben.");
                } else if(displayName.equalsIgnoreCase("§c§lWartungen")) {
                    if(configuration.isInMaintenance()) {
                        configuration.setMaintenance(false);
                        playerAPI.sendMessage(PlayerAPI.MessageType.WARNING, ChatMessageType.CHAT, "Die Wartungsarbeiten wurden deaktiviert.");
                    } else {
                        configuration.setMaintenance(true);
                        playerAPI.sendMessage(PlayerAPI.MessageType.WARNING, ChatMessageType.CHAT, "Die Wartungsarbeiten wurden aktiviert.");
                    }
                } else if(displayName.equalsIgnoreCase("§b§lHosting Pakete")) {
                    event.getWhoClicked().closeInventory();
                    Inventory inventory = Bukkit.createInventory(null, 5*9, "§b§lHosting Pakete");
                    for(int i = 0; i < inventory.getSize(); i++) {
                        inventory.setItem(i, pane);
                    }
                    inventory.setItem(13, new ItemUtil("§a§l4096mb", Material.EMERALD, "", "", "§6Kosten: §b80 Coins /Tag\n", "", "§cDieses Paket läuft nach 24h Serverlaufzeit ab.\n", "").buildItem());
                    inventory.setItem(20, new ItemUtil("§6§l2048mb", Material.GOLD_INGOT, "", "", "§6Kosten: §b40 Coins /Tag\n", "", "§cDieses Paket läuft nach 24h Serverlaufzeit ab.\n", "").buildItem());
                    inventory.setItem(24, new ItemUtil("§b§l3072mb", Material.DIAMOND, "", "", "§6Kosten: §b60 Coins /Tag\n", "", "§cDieses Paket läuft nach 24h Serverlaufzeit ab.\n", "").buildItem());
                    inventory.setItem(28, new ItemUtil("§7§l768mb", Material.COAL, "", "", "§6Kosten: §b0 Coins /Tag\n", "", "§cDieses Paket wird automatisch nach ablauf\n", "des gemieteten Paketes verwendet.\n", "").buildItem());
                    inventory.setItem(31, new ItemUtil("§f§l1536mb", Material.IRON_INGOT, "", "", "§6Kosten: §b30 Coins /Tag\n", "", "§cDieses Paket läuft nach 24h Serverlaufzeit ab.\n", "").buildItem());
                    inventory.setItem(34, new ItemUtil("§7§l1024mb", Material.BRICK, "", "", "§6Kosten: §b20 Coins /Tag\n", "", "§cDieses Paket läuft nach 24h Serverlaufzeit ab.\n", "").buildItem());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(ServerLibrary.getPlugin(), () -> event.getWhoClicked().openInventory(inventory), 3);
                } else if(displayName.equalsIgnoreCase("§c§lServer Stoppen")) {
                    PrivateServerManager manager = new PrivateServerManager(playerAPI);
                    manager.stopInstance();
                    event.getWhoClicked().closeInventory();
                    playerAPI.sendMessage(PlayerAPI.MessageType.NORMAL, ChatMessageType.CHAT, "Dein Server wurde heruntergefahren.");
                } else if(displayName.equalsIgnoreCase("§b§lVerbinden")) {
                    event.getWhoClicked().closeInventory();
                    new BungeeCord().sendPlayer(playerAPI.getAPIPlayer().getName(), new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceName());
                } else if(displayName.equalsIgnoreCase("§a§lServer Starten")) {
                    PrivateServerManager manager = new PrivateServerManager(playerAPI);
                    manager.createInstance();
                    playerAPI.sendMessage(PlayerAPI.MessageType.NORMAL, ChatMessageType.CHAT, "Dein Server wird gestartet. Bitte warten...");
                    event.getWhoClicked().closeInventory();
                    new AsyncThread(() -> new BungeeCord().sendPlayer(playerAPI.getAPIPlayer().getName(), new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceName())).runAsyncTaskLater(25);
                }
            } else if(title.equalsIgnoreCase("§a§lVorlagen")) {
                if(displayName.equalsIgnoreCase("§cZurück")) {
                    event.getWhoClicked().closeInventory();
                    Inventory inventory4 = Bukkit.createInventory(null, 5*9, "§dWas möchtest du Spielen?");
                    for(int i = 0; i < inventory4.getSize(); i++) {
                        inventory4.setItem(i, pane);
                    }
                    inventory4.setItem(11, new ItemUtil("§a§lVorlagen", Material.PAPER, "", "§bWähle einen Server aus Vorlagen aus und passe ihn\n", "nach deinen Wünschen an.\n", "").buildItem());
                    inventory4.setItem(15, new ItemUtil("§b§lCustom", Material.CHEST, "", "§bErstelle deinen eigenen Server, ganz ohne Vorlagen.\n", "").buildItem());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(ServerLibrary.getPlugin(), () -> event.getWhoClicked().openInventory(inventory4), 3);
                }
            } else if(title.equalsIgnoreCase("§b§lServerliste")) {
                if(displayName.equalsIgnoreCase("§bServer Suchen (Spieler Name)")) {
                    event.getWhoClicked().closeInventory();
                    playerAPI.sendMessage(PlayerAPI.MessageType.NORMAL, ChatMessageType.CHAT, "Bitte schreibe nun den Namen vom Spieler in den Chat.");
                    search.put((Player) event.getWhoClicked(), true);
                    motdSet.put((Player) event.getWhoClicked(), false);
                } else if(displayName.equalsIgnoreCase("§a§lServerliste")) {
                    event.getWhoClicked().closeInventory();
                    Inventory inventory = Bukkit.createInventory(null, 6*9, "§a§lSmaragd-Paket Serverliste");
                    for(int i = 0; i < inventory.getSize(); i++) {
                        inventory.setItem(i, pane);
                    }
                    List<ServiceInfoSnapshot> snapshots = new PrivateServerManager(playerAPI).getAllInstances();
                    for(int i = 0; i < (inventory.getSize()-9); i++) {
                        if(snapshots.get(i) != null) {
                            inventory.setItem(i, new ItemUtil(snapshots.get(i).getName(), Material.GREEN_STAINED_GLASS_PANE, "", "§f\"" + new PrivateServerConfigurationMySQL(getInstanceUUID(snapshots.get(i).getName())).getMOTD() + "\"", "", "§aDieser Server gehört: §b" + getPlayerNameFromUUID(getInstanceOwnerUUID(getInstanceUUID(snapshots.get(i).getName()))) + "", "").buildItem());
                        }
                    }
                    new AsyncThread(() -> event.getWhoClicked().openInventory(inventory));
                }
            } else if(title.equalsIgnoreCase("§dWas möchtest du Spielen?")) {
                if(displayName.equalsIgnoreCase("§a§lVorlagen")) {
                    event.getWhoClicked().closeInventory();
                    //add template getter method
                    Inventory inventory3 = Bukkit.createInventory(null, 5*9, "§a§lVorlagen");
                    for(int i = 0; i < inventory3.getSize(); i++) {
                        inventory3.setItem(i, pane);
                    }
                    inventory3.setItem(0, new ItemUtil("§cZurück", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(ServerLibrary.getPlugin(), () -> event.getWhoClicked().openInventory(inventory3), 3);
                } else if(displayName.equalsIgnoreCase("§b§lCustom")) {
                    event.getWhoClicked().closeInventory();
                    Inventory inventory2 = Bukkit.createInventory(null, 6*9, "§6§lEinstellungen");
                    for(int i = 0; i < inventory2.getSize(); i++) {
                        inventory2.setItem(i, pane);
                    }
                    inventory2.setItem(20, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                    inventory2.setItem(21, new ItemUtil("§6§lMOTD", Material.BOOK, "", "§aÄndere die MOTD\n", "", "Aktuelle MOTD: " + new PrivateServerConfigurationMySQL(new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceUUID()).getMOTD() + "", "").buildItem());
                    String string;
                    boolean maintenance = new PrivateServerConfigurationMySQL(new PrivateServerMySQL((Player) event.getWhoClicked()).getInstanceUUID()).isInMaintenance();
                    if(maintenance) {
                        string = "§a§lJa";
                    } else {
                        string = "§c§lNein";
                    }
                    inventory2.setItem(22, new ItemUtil("§c§lWartungen", Material.REPEATER, "", "§aSetze den Server in den Wartungsmodus\n", "", "§bAktiviert: §e" + string).buildItem());
                    inventory2.setItem(23, new ItemUtil("§b§lHosting Pakete", Material.BEACON, "", "§aMiete ein Hosting Paket von Coins\n", "").buildItem());
                    inventory2.setItem(24, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                    inventory2.setItem(29, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                    inventory2.setItem(30, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                    inventory2.setItem(31, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                    inventory2.setItem(32, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                    inventory2.setItem(33, new ItemUtil("", Material.RED_STAINED_GLASS_PANE, "").buildItem());
                    if(new PrivateServerMySQL((Player) event.getWhoClicked()).isInstanceOnline()) {
                        inventory2.setItem(53, new ItemUtil("§c§lServer Stoppen", Material.RED_DYE, "").buildItem());
                        inventory2.setItem(8, new ItemUtil("§b§lVerbinden", Material.ENDER_PEARL, "").buildItem());
                    } else {
                        inventory2.setItem(53, new ItemUtil("§a§lServer Starten", Material.LIME_DYE, "").buildItem());
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(ServerLibrary.getPlugin(), () -> event.getWhoClicked().openInventory(inventory2), 3);
                }
            }
        }
    }

    private static String getPlayerUUIDFromName(String name) throws IOException {
        StringBuilder jsonS = new StringBuilder();
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+name);
        URLConnection conn = url.openConnection();
        conn.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;

        while((inputLine = in.readLine()) != null) {
            jsonS.append(inputLine);
        }
        Gson gson = new Gson();
        JsonObject jsonObject= gson.fromJson(jsonS.toString(), JsonObject.class);
        String id = jsonObject.get("id").getAsString();
        in.close();
        return id;
    }

    private static String getPlayerNameFromUUID(String uuid) throws IOException {
        StringBuilder jsonS = new StringBuilder();
        URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid);
        URLConnection conn = url.openConnection();
        conn.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;

        while((inputLine = in.readLine()) != null) {
            jsonS.append(inputLine);
        }
        Gson gson = new Gson();
        JsonObject jsonObject= gson.fromJson(jsonS.toString(), JsonObject.class);
        String name = jsonObject.get("name").getAsString();
        in.close();
        return name;
    }

    private static String getInstanceNameFromPlayerUUID(String uuid) {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM privateServer WHERE playerUUID = '" + uuid + "';");
        try {
            if(rs.next()) {
                return rs.getString("instanceName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getInstanceOwnerUUID(String uuid) {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM privateServer WHERE instanceUUID = '" + uuid + "';");
        try {
            if(rs.next()) {
                return rs.getString("playerUUID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getInstanceUUID(String name) {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM privateServer WHERE instanceName = '" + name + "';");
        try {
            if(rs.next()) {
                return rs.getString("instanceUUID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
