package me.refluxo.gamehost.controller;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

@Plugin(
        id = "gamehost",
        name = "GameHostController",
        version = "1.0-SNAPSHOT"
)
public class GameHostController {

    private static Logger logger;
    private static ProxyServer server;

    @Inject
    public GameHostController(ProxyServer server, Logger logger) {
        GameHostController.server = server;
        GameHostController.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }

    public static Logger getLogger() {
        return logger;
    }

    public static ProxyServer getServer() {
        return server;
    }

}
