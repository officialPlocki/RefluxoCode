package me.refluxo.serverlibary.util.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;

import java.util.Objects;

public class CommandBuilder {

    private final Command command;

    public CommandBuilder(Command command) {
        this.command = command;
    }

    public void register() {
        Bukkit.getCommandMap().register(command.getName(), command);
    }

    public void unregister() {
        Objects.requireNonNull(Bukkit.getCommandMap().getCommand(command.getName())).unregister(Bukkit.getCommandMap());
    }

}
