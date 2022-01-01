package me.refluxo.rpglibrary.util.quests;

import lombok.Getter;
import org.bukkit.event.Event;

@Getter
@SuppressWarnings("unused")
public class Quest {

    private final int id;
    private final String name;
    private final String description;
    private final EventAdapter adapter;
    private final Class<? extends Event> eventClass;
    private int tmpID;

    public Quest(int id, String name, String description, Class<? extends Event> eventClass, EventAdapter adapter) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.adapter = adapter;
        this.eventClass = eventClass;
    }

}
