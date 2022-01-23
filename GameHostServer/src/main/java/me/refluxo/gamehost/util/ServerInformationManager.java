package me.refluxo.gamehost.util;

import me.refluxo.serverlibrary.util.files.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ServerInformationManager {

    private static YamlConfiguration yamlConfiguration;
    private static File file;

    public static void init() {
        file = new File("config/PSConfig/config.why");
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()) {
            try {
                file.createNewFile();
                yamlConfiguration.set("ownerUUID", "");
                yamlConfiguration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getOwnerUUID() {
        return yamlConfiguration.getString("ownerUUID");
    }

    public void setOwnerUUID(String uuid) {
        yamlConfiguration.set("ownerUUID", uuid);
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
