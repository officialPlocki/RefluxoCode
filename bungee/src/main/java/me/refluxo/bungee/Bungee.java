package me.refluxo.bungee;

import com.google.inject.Inject;
import com.velocitypowered.api.event.lifecycle.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import me.refluxo.bungee.util.TestListener;
import org.slf4j.Logger;

@Plugin(
        id = "bungee",
        name = "Bungee",
        version = "1.0-SNAPSHOT"
)
public class Bungee {

    private final Logger logger;
    private final ProxyServer server;

    @Inject
    public Bungee(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.eventManager().register(this, new TestListener());
    }
}
