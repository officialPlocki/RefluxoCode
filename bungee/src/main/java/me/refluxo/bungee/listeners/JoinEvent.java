package me.refluxo.bungee.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import me.refluxo.bungee.util.OnlineTime;

import java.sql.SQLException;
import java.util.EventListener;

public class JoinEvent implements EventListener {

    @Subscribe
    public void onJoin(LoginEvent event) {
        try {
            new OnlineTime(event.getPlayer()).checkPlayer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
