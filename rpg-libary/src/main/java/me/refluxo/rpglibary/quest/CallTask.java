package me.refluxo.rpglibary.quest;

import org.bukkit.event.Event;

public interface CallTask<T extends Event> {

    void call(T event);

}
