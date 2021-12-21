package me.refluxo.discord.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class PingCommand extends ListenerAdapter {

    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if(event.getName().equals("ping")) {
            long time = System.currentTimeMillis();
            event.getChannel().sendMessageFormat("Der Bot hat aktuell einen Ping von %s ms.", System.currentTimeMillis() - time).queue();
        }
    }

}
