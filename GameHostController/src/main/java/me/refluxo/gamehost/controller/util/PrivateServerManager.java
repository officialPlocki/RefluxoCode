package me.refluxo.gamehost.controller.util;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import me.refluxo.bungee.util.sql.MySQLService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PrivateServerManager {

    public List<ServiceInfoSnapshot> getAllInstances() {
        List<ServiceInfoSnapshot> snapshots = new ArrayList<>();
        ResultSet rs = new MySQLService().getResult("SELECT * FROM privateServer;");
        try {
            while (rs.next()) {
                if(rs.getBoolean("instanceIsOnline")) {
                    snapshots.add(CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(rs.getString("instanceName")));
                }
            }
            return snapshots;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return snapshots;
    }

    public void stopInstance(String serviceUniqueID, String instanceName) {
        CloudNetDriver.getInstance().getCloudServiceProvider(serviceUniqueID).stop();
        ResultSet rs = new MySQLService().getResult("SELECT * FROM privateServer WHERE instanceName = '" + instanceName + "';");
        String playerUUID = "";
        try {
            if(rs.next()) {
                playerUUID = rs.getString("playerUUID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new MySQLService().executeUpdate("UPDATE privateServer SET instanceIsOnline = false WHERE playerUUID = '" + playerUUID + "';");
    }

}
