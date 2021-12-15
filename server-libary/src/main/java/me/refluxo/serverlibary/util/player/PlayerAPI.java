package me.refluxo.serverlibary.util.player;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import me.refluxo.serverlibary.util.cloud.BungeeCord;
import me.refluxo.serverlibary.util.tablist.Rank;
import me.refluxo.serverlibary.util.tablist.RankManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PlayerAPI {

    private final Player player;

    public PlayerAPI(Player player) {
        this.player = player;
    }

    @Deprecated
    public void sendToServer(String server) {
        new BungeeCord().sendPlayer(player, server);
    }

    @Deprecated
    public void kickPlayerFromNetwork(String message) {
        new BungeeCord().kickPlayer(player, message);
    }

    public void kickPlayerFromServer(String message) {
        player.kickPlayer(message);
    }

    @Deprecated
    public void log(String messageToLog) {
        //mysql player log
    }

    public APIPlayer getAPIPlayer() {
        return new APIPlayer() {
            @Override
            public String getPrefix() {
                return getRank().getPrefix();
            }

            @Override
            public String getSuffix() {
                return getRank().getSuffix();
            }

            @Override
            public int getTablistHeight() {
                return getRank().getTablistHeight();
            }

            @Override
            public Rank getRank() {
                if(player.hasPermission("refluxo.administrator")) {
                    return new RankManager().getRank("administrator");
                } else if(player.hasPermission("refluxo.developer")) {
                    return new RankManager().getRank("developer");
                } else if(player.hasPermission("refluxo.content")) {
                    return new RankManager().getRank("content");
                } else if(player.hasPermission("refluxo.designer")) {
                    return new RankManager().getRank("designer");
                } else if(player.hasPermission("refluxo.moderator")) {
                    return new RankManager().getRank("moderator");
                } else if(player.hasPermission("refluxo.builder")) {
                    return new RankManager().getRank("builder");
                } else if(player.hasPermission("refluxo.supporter")) {
                    return new RankManager().getRank("supporter");
                } else if(player.hasPermission("refluxo.freund")) {
                    return new RankManager().getRank("freund");
                } else if(player.hasPermission("refluxo.promoter")) {
                    return new RankManager().getRank("promoter");
                } else if(player.hasPermission("refluxo.premium")) {
                    return new RankManager().getRank("premium");
                } else {
                    return new RankManager().getRank("spieler");
                }
            }

            @Override
            public String getCurrentServer() {
                if(player.isOnline()) {
                    return Objects.requireNonNull(CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).getOnlinePlayer(player.getUniqueId())).getNetworkConnectionInfo().getNetworkService().getTaskName();
                } else {
                    return Objects.requireNonNull(CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).getOfflinePlayer(player.getUniqueId())).getLastNetworkConnectionInfo().getNetworkService().getTaskName();
                }
            }

            @Override
            public String getOnlineTime() {
                return null;
            }

            @Override
            public String getAddress() {
                if(player.isOnline()) {
                    return Objects.requireNonNull(CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).getOnlinePlayer(player.getUniqueId())).getNetworkConnectionInfo().getAddress().getHost();
                } else {
                    return Objects.requireNonNull(CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).getOfflinePlayer(player.getUniqueId())).getLastNetworkConnectionInfo().getAddress().getHost();
                }
            }

            @Override
            public String getUniqueID() {
                return player.getUniqueId().toString();
            }

            @Override
            public String getUUID() {
                return player.getUniqueId().toString();
            }

            @Override
            public Location getLocation() {
                return player.getLocation();
            }

            @Override
            public Player getBukkitPlayer() {
                return player;
            }
        };
    }

}
