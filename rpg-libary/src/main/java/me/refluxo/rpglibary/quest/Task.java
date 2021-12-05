package me.refluxo.rpglibary.quest;

import org.bukkit.event.Event;

public class Task<T extends Event> {

    private final CallTask<T> task;

    public Task(CallTask<T> task) {
        this.task = task;
    }

    public T task() {
        return (T) task;
    }

    public static void init() {

    }

}
