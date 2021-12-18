package me.refluxo.serverlibary.util.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class NpcAPI {

    public NPC createNPC(String world, Location location, String displayName, String texture) {
        MinecraftServer dedicatedServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) Objects.requireNonNull(Bukkit.getWorld(world))).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), displayName);
        EntityPlayer npc = new EntityPlayer(dedicatedServer, worldServer, gameProfile);
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        gameProfile.getProperties().put("textures", new Property("textures", texture));
        return new NPC() {
            @Override
            public EntityPlayer getCraftEntity() {
                return npc;
            }

            @Override
            public Entity getBukkitEntity() {
                return npc.getBukkitEntity();
            }

            @Override
            public int getEntityID() {
                return npc.getBukkitEntity().getEntityId();
            }
        };
    }

    public void addNPCPacket(NPC npc) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            addNPCPacket(player, npc);
        });
    }

    public void addNPCPacket(Player player, NPC npc) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc.getCraftEntity()));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc.getCraftEntity()));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc.getCraftEntity(), (byte) (npc.getCraftEntity().getBukkitYaw() * 256 / 360)));
    }

    public void deleteNPC(Player player, NPC npc) {
        PlayerConnection connection = ((CraftPlayer)player).getHandle().b;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.b, npc.getCraftEntity()));
        npc.getBukkitEntity().remove();
    }

    public void deleteNPC(NPC npc) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerConnection connection = ((CraftPlayer)player).getHandle().b;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.b, npc.getCraftEntity()));
        });
        npc.getBukkitEntity().remove();
    }

    public void moveNPC(NPC npc, Location location) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerConnection connection = ((CraftPlayer)player).getHandle().b;
            npc.getCraftEntity().setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.c, npc.getCraftEntity()));
        });
    }

    public NPC getNPC(int entityID) {
        return new NPC() {
            @Override
            public EntityPlayer getCraftEntity() {
                return ((EntityPlayer) ((CraftWorld) Objects.requireNonNull(Bukkit.getWorld(""))).getHandle().getEntity(entityID));
            }

            @Override
            public Entity getBukkitEntity() {
                return getCraftEntity().getBukkitEntity();
            }

            @Override
            public int getEntityID() {
                return entityID;
            }
        };
    }

}
