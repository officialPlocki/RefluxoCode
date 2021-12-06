package me.refluxo.serverlibary.util.cloud;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.refluxo.serverlibary.ServerLibary;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class BungeeCord {

    public void sendPlayer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(Objects.requireNonNull(player.getPlayer()).getName());
        out.writeUTF(server);
        Bukkit.getServer().sendPluginMessage(ServerLibary.getPlugin(), "BungeeCord", out.toByteArray());
    }

    public void kickPlayer(Player player, String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("KickPlayer");
        out.writeUTF(Objects.requireNonNull(player.getPlayer()).getName());
        out.writeUTF(message);
        Bukkit.getServer().sendPluginMessage(ServerLibary.getPlugin(), "BungeeCord", out.toByteArray());
    }

}
