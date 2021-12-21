package me.refluxo.rpglibrary.util.cloud;

import me.refluxo.serverlibrary.util.cloud.GameInstance;

public class DungeonInstance {

    public GameInstance create(String ident, String uuid, int memory) {
        return new GameInstance(ident, uuid, memory);
    }

}
