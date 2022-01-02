package me.refluxo.gamehost.lobby.util;

import me.refluxo.serverlibrary.util.sql.MySQLService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrivateServerConfigurationMySQL {

    private final String serverUUID;

    public PrivateServerConfigurationMySQL(String serverUUID) {
        this.serverUUID = serverUUID;
    }

    public boolean needSetup() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM serverConfiguration WHERE serverUUID = '" + serverUUID + "';");
        try {
            if(!rs.next()) {
                new MySQLService().executeUpdate("INSERT INTO serverConfiguration(serverUUID,serverRAM,serverPackageTime,donatedCoins,serverMOTD,maintenance,serverOwner) VALUES ('" + serverUUID + "',768,0,0,'Diese MOTD ist noch nicht aufgesetzt.',true,'');");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setServerRAM(int ram) {
        new MySQLService().executeUpdate("UPDATE serverConfiguration SET serverRAM = " + ram + " WHERE serverUUID = '" + serverUUID + "';");
    }

    public int getRAM() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM serverConfiguration WHERE serverUUID = '" + serverUUID + "';");
        try {
            if(rs.next()) {
                return rs.getInt("serverRAM");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void resetPackageTime() {
        new MySQLService().executeUpdate("UPDATE serverConfiguration SET serverPackageTime = 86400 WHERE serverUUID = '" + serverUUID + "';");
    }

    public int getPackageTime() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM serverConfiguration WHERE serverUUID = '" + serverUUID + "';");
        try {
            if(rs.next()) {
                return rs.getInt("serverPackageTime");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addDonatedCoins(int coins) {
        new MySQLService().executeUpdate("UPDATE serverConfiguration SET donatedCoins = " + (getDonatedCoins()+coins) + " WHERE serverUUID = '" + serverUUID + "';");
    }

    public void removeDonatedCoins(int coins) {
        new MySQLService().executeUpdate("UPDATE serverConfiguration SET donatedCoins = " + (getDonatedCoins()-coins) + " WHERE serverUUID = '" + serverUUID + "';");
    }

    public int getDonatedCoins() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM serverConfiguration WHERE serverUUID = '" + serverUUID + "';");
        try {
            if(rs.next()) {
                return rs.getInt("donatedCoins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setMOTD(String motd) {
        new MySQLService().executeUpdate("UPDATE serverConfiguration SET serverMOTD = '" + motd + "' WHERE serverUUID = '" + serverUUID + "';");
    }

    public String getMOTD() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM serverConfiguration WHERE serverUUID = '" + serverUUID + "';");
        try {
            if(rs.next()) {
                return rs.getString("serverMOTD");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setOwnerUUID(String uuid) {
        new MySQLService().executeUpdate("UPDATE serverConfiguration SET serverOwner = '" + uuid + "' WHERE serverUUID = '" + serverUUID + "';");
    }

    public void setMaintenance(boolean maintenance) {
        new MySQLService().executeUpdate("UPDATE serverConfiguration SET maintenance = " + maintenance + " WHERE serverUUID = '" + serverUUID + "';");
    }

    public boolean isInMaintenance() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM serverConfiguration WHERE serverUUID = '" + serverUUID + "';");
        try {
            if(rs.next()) {
                return rs.getBoolean("maintenance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getOwnerUUID() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM serverConfiguration WHERE serverUUID = '" + serverUUID + "';");
        try {
            if(rs.next()) {
                return rs.getString("serverOwner");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void init() {
        new MySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS serverConfiguration(serverUUID TEXT, serverRAM INT, serverPackageTime INT, donatedCoins INT, serverMOTD TEXT, maintenance BOOLEAN, serverOwner TEXT);");
    }

}
