package me.refluxo.serverlibary.util.tablist;

import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class TablistManager {

    private static Scoreboard scoreboard;
    private final HashMap<UUID, String> teams;

    public TablistManager() {
        scoreboard = new Scoreboard();
        teams = new HashMap<>();
    }

    public void registerTeam(Player player, Rank rank) {
        String s = rank.getTablistHeight() + player.getUniqueId().toString();
        if(scoreboard.getTeam(s) != null) {
            scoreboard.removeTeam(scoreboard.getTeam(s));
        }
        ScoreboardTeam team = scoreboard.createTeam(s);
        team.setColor(rank.getColor());
        team.setPrefix(IChatBaseComponent.a(rank.getPrefix()));
        team.setSuffix(IChatBaseComponent.a(rank.getSuffix()));

        teams.put(player.getUniqueId(), s);
        update();
    }

    private void update() {

        Bukkit.getOnlinePlayers().forEach(player -> {
            if(!Objects.requireNonNull(scoreboard.getTeam(teams.get(player.getUniqueId()))).getPlayerNameSet().contains(player.getName())) {
                Objects.requireNonNull(scoreboard.getTeam(teams.get(player.getUniqueId()))).getPlayerNameSet().add(player.getName());
            }
            sendPacket(PacketPlayOutScoreboardTeam.a(Objects.requireNonNull(scoreboard.getTeam(teams.get(player.getUniqueId())))));
        });

    }

    private void sendPacket(Packet<?> packet) {
        Bukkit.getOnlinePlayers().forEach(player -> ((CraftPlayer)player).getHandle().b.sendPacket(packet));
    }

}
