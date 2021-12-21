package me.refluxo.serverlibrary.util.player;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

    private static final Map<Player, PlayerAPI> players = new HashMap<>();

    public void registerPlayer(PlayerAPI player) {
        if(!players.containsKey(player.getAPIPlayer().getBukkitPlayer())) {
            players.put(player.getAPIPlayer().getBukkitPlayer(), player);
        }
    }

    public PlayerAPI getPlayer(Player player) {
        if(players.containsKey(player)) {
            players.put(player, new PlayerAPI(player));
        }
        return players.get(player);
    }

    public void unregisterPlayer(Player player) {
        players.remove(player);
    }

    public List<PlayerAPI> getOnlinePlayers() {
        return players.values().stream().toList();
    }

}
