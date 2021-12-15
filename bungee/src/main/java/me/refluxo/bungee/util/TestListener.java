package me.refluxo.bungee.util;

import com.velocitypowered.api.event.Event;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.LoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import me.refluxo.bungee.Bungee;

public class TestListener implements Event {

    @Subscribe
    public void onPlayerChat(LoginEvent event) {

    }

}
