package me.refluxo.bungee;

import com.google.inject.Inject;
import com.velocitypowered.api.event.lifecycle.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import me.refluxo.bungee.listeners.JoinEvent;
import me.refluxo.bungee.util.OnlineTime;
import org.slf4j.Logger;

@Plugin(
        id = "bungee",
        name = "Bungee",
        version = "1.0-SNAPSHOT"
)
public class Bungee {

    private static Logger logger = null;
    private static ProxyServer server = null;
    private static Bungee instance;

    @Inject
    public Bungee(ProxyServer server, Logger logger) {
        instance = this;
        Bungee.server = server;
        Bungee.logger = logger;
        server.eventManager().register(this, new JoinEvent());
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        assert server != null;
        OnlineTime.init();
    }

    public static Logger getLogger() { return logger; }

    public static ProxyServer getProxyServer() { return server; }

    public static Bungee getInstance() {
        return instance;
    }

}
