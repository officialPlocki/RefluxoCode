package me.refluxo.discord.bot.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEvent extends ListenerAdapter {

    public void onReceive(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Anwesend!").queue();
    }

}
