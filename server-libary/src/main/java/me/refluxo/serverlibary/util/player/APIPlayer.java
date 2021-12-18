package me.refluxo.serverlibary.util.player;

import me.refluxo.serverlibary.util.score.rank.Rank;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface APIPlayer {

    Rank getRank();

    String getName();

    String getCurrentServer();

    String getOnlineTime();

    String getAddress();

    String getUniqueID();

    String getUUID();

    Location getLocation();

    Player getBukkitPlayer();

}
