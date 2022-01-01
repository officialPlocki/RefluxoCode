package me.refluxo.discord.bot;

import me.refluxo.discord.bot.commands.PingCommand;
import me.refluxo.discord.bot.events.MessageEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class RefluxoBot {

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = JDABuilder.createLight("OTIyNjQ0ODEyOTA0MTY5NTEy.YcEd8Q.rWBDuAx_KC1K9lb9jzMLub7FAi0");
        builder.setActivity(Activity.of(Activity.ActivityType.LISTENING, "plocki beim verzweifeln"));
        builder.addEventListeners(new MessageEvent());
        builder.addEventListeners(new PingCommand());
        JDA jda = builder.build();

        jda.upsertCommand("ping", "Zeigt den Ping vom Bot an.").queue();
    }

}
