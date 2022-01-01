package me.refluxo.serverlibrary.listeners;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceConnectNetworkEvent;
import me.refluxo.serverlibrary.ServerLibrary;
import org.bukkit.event.Listener;

public class CloudNetServiceConnectListener implements Listener {

    @EventListener
    public void onConnect(CloudServiceConnectNetworkEvent event) {
        ServerLibrary.instanceName = event.getServiceInfo().getName();
    }

}
