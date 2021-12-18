package me.refluxo.serverlibary.util.npc;

import net.minecraft.server.level.EntityPlayer;
import org.bukkit.entity.Entity;

public interface NPC {

    EntityPlayer getCraftEntity();

    Entity getBukkitEntity();

    int getEntityID();

}
