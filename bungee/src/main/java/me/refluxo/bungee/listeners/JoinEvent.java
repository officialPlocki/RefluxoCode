package me.refluxo.bungee.listeners;

import com.velocitypowered.api.event.Event;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.LoginEvent;
import me.refluxo.bungee.util.OnlineTime;

import java.sql.SQLException;

public class JoinEvent implements Event {

    @Subscribe
    public void onJoin(LoginEvent event) {
        try {
            new OnlineTime(event.player()).checkPlayer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
