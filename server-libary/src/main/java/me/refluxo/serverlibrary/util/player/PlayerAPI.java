package me.refluxo.serverlibrary.util.player;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import me.refluxo.serverlibrary.util.cloud.BungeeCord;
import me.refluxo.serverlibrary.util.score.rank.Rank;
import me.refluxo.serverlibrary.util.score.rank.RankManager;
import me.refluxo.serverlibrary.util.sql.MySQLService;
import me.refluxo.serverlibrary.util.sql.log.MySQLLog;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public record PlayerAPI(Player player) {

    public enum MessageType {
        NORMAL,
        WARNING,
        ERROR,
        ANNOUNCEMENT
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

    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    public void log(String messageToLog) {
        new MySQLLog().log(player, messageToLog);
    }

    public void sendMessage(MessageType type, ChatMessageType position, String msg) {
        String typemsg;
        if (type == MessageType.ANNOUNCEMENT) {
            typemsg = "§c§lANNOUNCEMENT §8» §7";
        } else if (type == MessageType.ERROR) {
            typemsg = "§c§lFEHLER §8» §7";
        } else if (type == MessageType.WARNING) {
            typemsg = "§e§lWARNUNG §8» §7";
        } else {
            typemsg = "";
        }
        if (position.equals(ChatMessageType.ACTION_BAR)) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 10);
        }
        player.spigot().sendMessage(position, TextComponent.fromLegacyText(typemsg + msg));
    }

    public APIPlayer getAPIPlayer() {
        return new APIPlayer() {

            @Override
            public Rank getRank() {
                if (player.hasPermission("refluxo.administrator")) {
                    return new RankManager().getRank("administrator");
                } else if (player.hasPermission("refluxo.developer")) {
                    return new RankManager().getRank("developer");
                } else if (player.hasPermission("refluxo.content")) {
                    return new RankManager().getRank("content");
                } else if (player.hasPermission("refluxo.designer")) {
                    return new RankManager().getRank("designer");
                } else if (player.hasPermission("refluxo.moderator")) {
                    return new RankManager().getRank("moderator");
                } else if (player.hasPermission("refluxo.builder")) {
                    return new RankManager().getRank("builder");
                } else if (player.hasPermission("refluxo.supporter")) {
                    return new RankManager().getRank("supporter");
                } else if (player.hasPermission("refluxo.freund")) {
                    return new RankManager().getRank("freund");
                } else if (player.hasPermission("refluxo.promoter")) {
                    return new RankManager().getRank("promoter");
                } else if (player.hasPermission("refluxo.premium")) {
                    return new RankManager().getRank("premium");
                } else {
                    return new RankManager().getRank("spieler");
                }
            }

            @Override
            public String getName() {
                return player.getName();
            }

            @Override
            public String getCurrentServer() {
                if (player.isOnline()) {
                    return Objects.requireNonNull(CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).getOnlinePlayer(player.getUniqueId())).getNetworkConnectionInfo().getNetworkService().getTaskName();
                } else {
                    return Objects.requireNonNull(CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).getOfflinePlayer(player.getUniqueId())).getLastNetworkConnectionInfo().getNetworkService().getTaskName();
                }
            }

            @Override
            public String getOnlineTime() {
                long time = 0;
                ResultSet rs = new MySQLService().getResult("SELECT * FROM onlineTime WHERE uuid = '" + player.getUniqueId() + "';");
                try {
                    if (rs.next()) {
                        time = rs.getLong("t");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return DurationFormatUtils.formatDuration(TimeUnit.SECONDS.toMillis(time), "HH:mm:ss");
            }

            @Override
            public String getAddress() {
                if (player.isOnline()) {
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
