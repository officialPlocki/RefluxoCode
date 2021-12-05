package me.refluxo.rpglibary.util;

import me.refluxo.rpglibary.quest.IQuest;
import me.refluxo.rpglibary.quest.Task;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class TestQuest implements IQuest<PlayerJoinEvent> {

    @Override
    public Task<PlayerJoinEvent> getTask() {
        return new Task<>(null);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Player getPlayer() {
        return getTask().task().getPlayer();
    }

}
