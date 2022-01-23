package me.refluxo.serverlibrary.util.player.antiafk;

import me.refluxo.serverlibrary.ServerLibrary;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AntiAFK implements Listener {

    private static final Map<Player, List<Location>> lastLocations = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        assert event.getTo() != null;
        if(event.getFrom().getX() != event.getTo().getX() && event.getFrom().getZ() != event.getTo().getZ()) {
            if(lastLocations.containsKey(event.getPlayer())) {
                List<Location> locations = lastLocations.get(event.getPlayer());
                locations.add(event.getTo());
                if(locations.size() >= 20) {
                    int same = 0;
                    for(Location location : locations) {
                        int x = (int) location.getX();
                        int y = (int) location.getY();
                        int z = (int) location.getZ();
                        float yaw = location.getYaw();
                        float pitch = location.getPitch();
                        for(Location check : locations) {
                            int cx = (int) check.getX();
                            int cy = (int) check.getY();
                            int cz = (int) check.getZ();
                            float cyaw = check.getYaw();
                            float cpitch = check.getPitch();
                            if(cx == x) {
                                if(cy == y) {
                                    if(cz == z) {
                                        same = same+1;
                                    }
                                }
                            }
                            if(cyaw == yaw) {
                                if(cpitch == pitch) {
                                    same = same+1;
                                }
                            }
                        }
                    }
                    if(same >= 10) {
                        event.getPlayer().kickPlayer(ServerLibrary.prefix + "Du warst zu lange AFK.");
                    }
                    locations.clear();
                    lastLocations.put(event.getPlayer(), locations);
                }
            } else {
                lastLocations.put(event.getPlayer(), List.of(event.getFrom(), event.getTo()));
            }
        }
    }

}
