package me.refluxo.serverlibary.util.player;

import me.refluxo.serverlibary.util.tablist.Rank;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface APIPlayer {

    String getPrefix();

    String getSuffix();

    int getTablistHeight();

    Rank getRank();

    String getCurrentServer();

    String getOnlineTime();

    String getAddress();

    String getUniqueID();

    String getUUID();

    Location getLocation();

    Player getBukkitPlayer();

}
