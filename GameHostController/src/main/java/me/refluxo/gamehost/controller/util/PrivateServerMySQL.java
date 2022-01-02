package me.refluxo.gamehost.controller.util;

import me.refluxo.bungee.util.sql.MySQLService;

public class PrivateServerMySQL {

    public void setInstanceOnline(boolean online, String playerUUID) {
        new MySQLService().executeUpdate("UPDATE privateServer SET instanceIsOnline = " + online + " WHERE playerUUID = '" + playerUUID + "';");
    }

}
