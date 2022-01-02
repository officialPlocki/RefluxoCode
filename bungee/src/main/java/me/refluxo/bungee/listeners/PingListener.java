package me.refluxo.bungee.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.event.query.ProxyQueryEvent;
import com.velocitypowered.api.proxy.server.QueryResponse;
import com.velocitypowered.api.proxy.server.ServerPing;
import me.refluxo.bungee.Bungee;
import net.kyori.adventure.text.Component;

import java.util.EventListener;
import java.util.Objects;
import java.util.stream.Collectors;

public class PingListener implements EventListener {

    @Subscribe
    public void onPing(ProxyPingEvent event) {
        long ping = System.currentTimeMillis();
        String pingAddress = event.getConnection().getRemoteAddress().getHostName();
        Bungee.getInstance().getAllPlayers().forEach(player -> {
            if(Objects.equals(pingAddress, player.getRemoteAddress().getHostName())) {
                event.setPing(ServerPing.builder()
                        .maximumPlayers(-1)
                        .description(Component.text("§a§lDein Ping: " + player.getPing() + " ms §e§l| §b§lSystem Ping: " + (ping - System.currentTimeMillis()) + " ms"))
                        .notModCompatible()
                        .build());
            }
        });
    }

    @Subscribe
    public void onQuery(ProxyQueryEvent event) {
        event.setResponse(QueryResponse.builder()
                .currentPlayers(Bungee.getInstance().getPlayerCount())
                .players(String.valueOf(Bungee.getInstance().getAllPlayers()))
                .hostname("192.168.178.39")
                .map(event.getResponse().getMap())
                .clearPlugins()
                .maxPlayers(event.getResponse().getMaxPlayers())
                .proxyHost("192.168.178.39")
                .gameVersion("1.17.1")
                .build());
    }

}
