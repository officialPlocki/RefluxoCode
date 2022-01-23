package me.refluxo.gamehost.listeners;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.refluxo.gamehost.util.PrivateServerManager;
import me.refluxo.serverlibrary.ServerLibrary;
import me.refluxo.serverlibrary.util.inventory.ItemUtil;
import me.refluxo.serverlibrary.util.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class InventoryClickListener implements Listener {

    private static final Map<HumanEntity, String> IDs = new HashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem() != null) {
            if(event.getCurrentItem().getItemMeta() != null) {
                String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
                String title = event.getView().getTitle();
                if(title.equalsIgnoreCase("§c§lGameHost §8» §a§lOptionen")) {
                    event.setCancelled(true);
                    if(displayName.equalsIgnoreCase("§c§lServer Stoppen")) {
                        new PrivateServerManager(new PlayerManager().getPlayer((Player) event.getWhoClicked())).stopInstance();
                    } else if(displayName.equalsIgnoreCase("§a§lPlugin installieren")) {
                        event.getWhoClicked().closeInventory();
                        Bukkit.getScheduler().scheduleSyncDelayedTask(ServerLibrary.getPlugin(), () -> {
                            if(!IDs.containsKey(event.getWhoClicked())) {
                                IDs.put(event.getWhoClicked(), "");
                            }
                            Inventory inv = getIDInventory(event.getWhoClicked());
                            if(inv == null) {
                                //PLUGIN SEARCH AND INSTALL ON SPIGOTMC
                            } else {
                                event.getWhoClicked().openInventory(inv);
                            }
                        }, 3);
                    }
                }
            }
        }
    }

    private void getJson(String link) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(link).openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
        if(!jsonObject.isJsonNull()) {
            JsonArray jsonArray = jsonObject.getAsJsonArray("id");
            if(!jsonArray.isJsonNull()) {
                JsonElement jsonElement = jsonArray.get(1);
            }
        }

    }

    private Inventory getIDInventory(HumanEntity entity) {
        String id = IDs.getOrDefault(entity, "");
        if(id.length() != 6) {
            Inventory inventory = Bukkit.createInventory(null, 3*9, "§aPlugin ID");
            ItemStack pane = new ItemUtil("§f", Material.BLACK_STAINED_GLASS_PANE, "").buildItem();
            for(int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, pane);
            }
            inventory.setItem(3, new ItemUtil("§b1", Material.BLUE_STAINED_GLASS_PANE, "").buildItem());
            inventory.setItem(4, new ItemUtil("§b1", Material.BLUE_STAINED_GLASS_PANE, "").buildItem());
            inventory.setItem(5, new ItemUtil("§b1", Material.BLUE_STAINED_GLASS_PANE, "").buildItem());
            inventory.setItem(12, new ItemUtil("§b1", Material.BLUE_STAINED_GLASS_PANE, "").buildItem());
            inventory.setItem(13, new ItemUtil("§b1", Material.BLUE_STAINED_GLASS_PANE, "").buildItem());
            inventory.setItem(14, new ItemUtil("§b1", Material.BLUE_STAINED_GLASS_PANE, "").buildItem());
            inventory.setItem(21, new ItemUtil("§b1", Material.BLUE_STAINED_GLASS_PANE, "").buildItem());
            inventory.setItem(22, new ItemUtil("§b1", Material.BLUE_STAINED_GLASS_PANE, "").buildItem());
            inventory.setItem(23, new ItemUtil("§b1", Material.BLUE_STAINED_GLASS_PANE, "").buildItem());

            inventory.setItem(8, new ItemUtil("§eEingabe: §7" + id, Material.BLUE_STAINED_GLASS_PANE, "").buildItem());
            inventory.setItem(18, new ItemUtil("§cAbbrechen", Material.RED_STAINED_GLASS_PANE, "").buildItem());
            inventory.setItem(26, new ItemUtil("§aBestätigen", Material.GREEN_STAINED_GLASS_PANE, "").buildItem());
            return inventory;
        } else {
            return null;
        }
    }

}
