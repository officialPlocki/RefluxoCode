package me.refluxo.rpglibary.quest;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public interface IQuest<T extends Event> {

    Task<T> getTask();

    boolean isFinished();

    Player getPlayer();

}
