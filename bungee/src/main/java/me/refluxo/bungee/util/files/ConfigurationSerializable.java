package me.refluxo.bungee.util.files;

import java.util.Map;

public interface ConfigurationSerializable {

    Map<String, Object> serialize();
}