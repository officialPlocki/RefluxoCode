package me.refluxo.rpglibrary.util.player.data;

import me.refluxo.rpglibrary.util.quests.Quest;
import me.refluxo.rpglibrary.util.quests.QuestManager;
import me.refluxo.serverlibrary.util.player.PlayerAPI;
import me.refluxo.serverlibrary.util.sql.MySQLService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLQuest {

    private final PlayerAPI player;

    public MySQLQuest(PlayerAPI player) {
        this.player = player;
    }

    public Quest updateQuest(boolean completed) {
        checkPlayer();
        new MySQLService().executeUpdate("UPDATE rpgQuest SET questID = " + (getActiveQuest().getId()+1) + " WHERE uuid = '" + player.getAPIPlayer().getUUID() + "';");
        return getActiveQuest();
    }

    public Quest getActiveQuest() {
        checkPlayer();
        ResultSet rs = new MySQLService().getResult("SELECT * FROM rpgQuest WHERE uuid = '" + player.getAPIPlayer().getUUID() + "';");
        try {
            if(rs.next()) {
                return new QuestManager().getQuest(rs.getInt("questID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void checkPlayer() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM rpgQuest WHERE uuid = '" + player.getAPIPlayer().getUUID() + "';");
        try {
            if(!rs.next()) {
                new MySQLService().executeUpdate("INSERT INTO rpgQuest(uuid,questID,questCompleted) VALUES ('" + player.getAPIPlayer().getUUID() + "',1,false);");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        new MySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS rpgQuest(uuid TEXT, questID INT, questCompleted BOOLEAN);");
    }

}
