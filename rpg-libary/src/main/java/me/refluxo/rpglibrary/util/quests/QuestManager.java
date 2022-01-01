package me.refluxo.rpglibrary.util.quests;

import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unused")
public class QuestManager {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static Map<Integer, Quest> quests;

    public void registerQuest(Quest quest) {
        quests.put(quest.getId(), quest);
    }

    public Quest getQuest(int id) {
        return quests.getOrDefault(id, null);
    }

    public boolean call(Event event){
        AtomicBoolean cancelled = new AtomicBoolean(false);
        for(Quest quest : quests.values()){
            if(quest.getEventClass().isInstance(event.getClass())) {
                if (quest.getAdapter().call(event)) {
                    cancelled.set(true);
                }
            }
        }
        return cancelled.get();
    }

    public static void init() {
        quests = new HashMap<>();
    }

}
