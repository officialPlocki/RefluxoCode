package me.refluxo.gamehost.controller.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceTemplate;
import me.refluxo.bungee.Bungee;
import me.refluxo.bungee.util.sql.MySQLService;
import xyz.plocki.asyncthread.AsyncThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GameHostControl {

    private static final Map<String, Integer> emptyType = new HashMap<>();

    public static void init() {
        new AsyncThread(() -> new PrivateServerManager().getAllInstances().forEach(serviceInfoSnapshot -> {
            int players = Bungee.getProxyServer().getServer(serviceInfoSnapshot.getName()).get().getPlayersConnected().size();
            if(players == 0) {
                if(emptyType.getOrDefault(serviceInfoSnapshot.getName(), 0) >= 5) {
                    emptyType.remove(serviceInfoSnapshot.getName());
                    new PrivateServerMySQL().setInstanceOnline(false, getInstanceOwnerUUID(getInstanceUUID(serviceInfoSnapshot.getName())));
                    new PrivateServerManager().stopInstance(serviceInfoSnapshot.getServiceId().getUniqueId().toString(), serviceInfoSnapshot.getName());
                } else {
                    emptyType.put(serviceInfoSnapshot.getName(), emptyType.getOrDefault(serviceInfoSnapshot.getName(), 0)+1);
                }
            } else {
                emptyType.remove(serviceInfoSnapshot.getName());
            }
        })).scheduleAsyncTask(0,60);

    }

    private static String getPlayerUUIDFromName(String name) throws IOException {
        StringBuilder jsonS = new StringBuilder();
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+name);
        URLConnection conn = url.openConnection();
        conn.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;

        while((inputLine = in.readLine()) != null) {
            jsonS.append(inputLine);
        }
        Gson gson = new Gson();
        JsonObject jsonObject= gson.fromJson(jsonS.toString(), JsonObject.class);
        String id = jsonObject.get("id").getAsString();
        in.close();
        return id;
    }

    private static String getPlayerNameFromUUID(String uuid) throws IOException {
        StringBuilder jsonS = new StringBuilder();
        URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid);
        URLConnection conn = url.openConnection();
        conn.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;

        while((inputLine = in.readLine()) != null) {
            jsonS.append(inputLine);
        }
        Gson gson = new Gson();
        JsonObject jsonObject= gson.fromJson(jsonS.toString(), JsonObject.class);
        String name = jsonObject.get("name").getAsString();
        in.close();
        return name;
    }

    private static String getInstanceNameFromPlayerUUID(String uuid) {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM privateServer WHERE playerUUID = '" + uuid + "';");
        try {
            if(rs.next()) {
                return rs.getString("instanceName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getInstanceOwnerUUID(String uuid) {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM privateServer WHERE instanceUUID = '" + uuid + "';");
        try {
            if(rs.next()) {
                return rs.getString("playerUUID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getInstanceUUID(String name) {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM privateServer WHERE instanceName = '" + name + "';");
        try {
            if(rs.next()) {
                return rs.getString("instanceUUID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
