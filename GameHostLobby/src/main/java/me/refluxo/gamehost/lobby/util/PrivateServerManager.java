package me.refluxo.gamehost.lobby.util;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import me.refluxo.serverlibrary.util.cloud.GameInstance;
import me.refluxo.serverlibrary.util.player.PlayerAPI;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PrivateServerManager {

    private final PlayerAPI playerAPI;

    public PrivateServerManager(PlayerAPI playerAPI) {
        this.playerAPI = playerAPI;
    }

    public ServiceInfoSnapshot createInstance() {
        GameInstance instance = new GameInstance("ps", new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).getInstanceUUID(), 1024);
        instance.startInstance();
        ServiceInfoSnapshot snapshot = instance.getInstance();
        new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).setInstanceName(snapshot.getName());
        new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).setInstanceOnline(true);
        return snapshot;
    }

    public ServiceInfoSnapshot getInstance() {
        if(!new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).isInstanceOnline()) {
            return null;
        } else {
            return CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).getInstanceName());
        }
    }

    public boolean isOnline() {
        return new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).isInstanceOnline();
    }

    public void stopInstance() {
        CloudNetDriver.getInstance().getCloudServiceProvider(getInstance().getServiceId().getUniqueId()).stop();
        new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).setInstanceOnline(false);
    }

}
