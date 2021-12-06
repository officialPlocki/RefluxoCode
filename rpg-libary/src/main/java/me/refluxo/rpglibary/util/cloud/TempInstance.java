package me.refluxo.rpglibary.util.cloud;

import me.refluxo.serverlibary.util.cloud.GameInstance;

public class TempInstance {

    public GameInstance create(String ident, String uuid, int memory) {
        return new GameInstance(ident, uuid, memory);
    }

}
