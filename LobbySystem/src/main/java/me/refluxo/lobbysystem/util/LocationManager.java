package me.refluxo.lobbysystem.util;

import me.refluxo.serverlibrary.util.files.FileBuilder;
import me.refluxo.serverlibrary.util.files.YamlConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationManager {

    public void saveLocation(String name, Location location) {
        FileBuilder builder = new FileBuilder("config/library/locations.yml");
        YamlConfiguration yml = builder.getYaml();
        yml.set(name + ".X", location.getX());
        yml.set(name + ".Y", location.getY());
        yml.set(name + ".Z", location.getZ());
        yml.set(name + ".YAW", location.getYaw());
        yml.set(name + ".PITCH", location.getPitch());
        yml.set(name + ".WORLD", location.getWorld().getName());
        builder.save();
    }

    public Location getLocation(String name) {
        FileBuilder builder = new FileBuilder("config/library/locations.yml");
        YamlConfiguration yml = builder.getYaml();
        return new Location(Bukkit.getWorld(yml.getString(name + ".WORLD")), yml.getDouble(name + ".X"), yml.getDouble(name + ".Y"), yml.getDouble(name + ".Z"), (float) yml.getDouble(name + ".YAW"), (float) yml.getDouble(name + ".PITCH"));
    }

}
