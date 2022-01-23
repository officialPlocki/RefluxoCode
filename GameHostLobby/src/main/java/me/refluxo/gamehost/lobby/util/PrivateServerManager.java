package me.refluxo.gamehost.lobby.util;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import me.refluxo.serverlibrary.util.cloud.GameInstance;
import me.refluxo.serverlibrary.util.player.PlayerAPI;
import me.refluxo.serverlibrary.util.sql.MySQLService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrivateServerManager {

    private final PlayerAPI playerAPI;

    public PrivateServerManager(PlayerAPI playerAPI) {
        this.playerAPI = playerAPI;
    }

    public void createInstance() {
        GameInstance instance = new GameInstance("ps", new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).getInstanceUUID(), new PrivateServerConfigurationMySQL(new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).getInstanceUUID()).getRAM());
        instance.addOwnerUUID(playerAPI.getAPIPlayer().getUUID());
        instance.startInstance();
        new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).setInstanceName("ps" + new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).getInstanceUUID() + "-1");
        new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).setInstanceOnline(true);
    }

    public ServiceInfoSnapshot getInstance() {
        if(!new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).isInstanceOnline()) {
            return null;
        } else {
            return CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).getInstanceName());
        }
    }

    public List<ServiceInfoSnapshot> getAllInstances() {
        List<ServiceInfoSnapshot> snapshots = new ArrayList<>();
        ResultSet rs = new MySQLService().getResult("SELECT * FROM privateServer;");
        try {
            while (rs.next()) {
                if(rs.getBoolean("instanceIsOnline")) {
                    if(new PrivateServerConfigurationMySQL(rs.getString("instanceUUID")).getRAM() >= 4096) {
                        snapshots.add(CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(rs.getString("instanceName")));
                    }
                }
            }
            return snapshots;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return snapshots;
    }

    public boolean isOnline() {
        return new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).isInstanceOnline();
    }

    public void stopInstance() {
        CloudNetDriver.getInstance().getCloudServiceProvider(getInstance().getServiceId().getUniqueId()).stop();
        new PrivateServerMySQL(playerAPI.getAPIPlayer().getBukkitPlayer()).setInstanceOnline(false);
    }

}
