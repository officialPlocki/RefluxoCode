package me.refluxo.serverlibrary.util.cloud;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.refluxo.serverlibrary.ServerLibrary;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

@SuppressWarnings("ALL")
public class BungeeCord {

    @Deprecated
    public void sendPlayer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(Objects.requireNonNull(player.getPlayer()).getName());
        out.writeUTF(server);
        Bukkit.getServer().sendPluginMessage(ServerLibrary.getPlugin(), "BungeeCord", out.toByteArray());
    }

    @Deprecated
    public void sendPlayer(String playerName, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(playerName);
        out.writeUTF(server);
        Bukkit.getServer().sendPluginMessage(ServerLibrary.getPlugin(), "BungeeCord", out.toByteArray());
    }

    @Deprecated
    public void kickPlayer(Player player, String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("KickPlayer");
        out.writeUTF(Objects.requireNonNull(player.getPlayer()).getName());
        out.writeUTF(message);
        Bukkit.getServer().sendPluginMessage(ServerLibrary.getPlugin(), "BungeeCord", out.toByteArray());
    }

}
