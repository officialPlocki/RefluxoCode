package me.refluxo.serverlibary.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.plocki.asyncthread.AsyncThread;

import java.util.Objects;

public class ChunkLoadEvent implements Listener {

    @EventHandler
    public void onLoad(org.bukkit.event.world.ChunkLoadEvent event) {
        if(!event.isAsynchronous()) {
            new AsyncThread(() -> {
                Objects.requireNonNull(Bukkit.getWorld(event.getWorld().getName())).loadChunk(event.getChunk());
            }).runAsync();
        }
    }

}
