package me.refluxo.serverlibrary.util.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.Objects;

public class CommandBuilder {

    private final Command command;


    public CommandBuilder(Command command) {
        this.command = command;
    }

    public void register() {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(command.getName(), command);
            bukkitCommandMap.setAccessible(false);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void unregister() {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            Objects.requireNonNull(commandMap.getCommand(command.getName())).unregister(commandMap);
            bukkitCommandMap.setAccessible(false);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
