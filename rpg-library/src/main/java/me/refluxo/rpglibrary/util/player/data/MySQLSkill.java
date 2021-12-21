package me.refluxo.rpglibrary.util.player.data;

import me.refluxo.rpglibrary.util.player.skills.ISkill;
import me.refluxo.rpglibrary.util.player.skills.SkillManager;
import me.refluxo.serverlibrary.util.player.PlayerAPI;
import me.refluxo.serverlibrary.util.sql.MySQLService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLSkill {

    private final PlayerAPI player;

    public MySQLSkill(PlayerAPI player) {
        this.player = player;
    }

    public void setSkillLevel(ISkill skill, int level) {
        checkPlayer();
        new MySQLService().executeUpdate("UPDATE rpgSkills SET level = " + level + " WHERE uuid = '" + player.getAPIPlayer().getUUID() + "' AND skillName = '" + skill.getSkillName()+"';");
    }

    public void setSkillLevelUPPercentage(ISkill skill, int percentage) {
        checkPlayer();
        new MySQLService().executeUpdate("UPDATE rpgSkills SET percentage = " + percentage + " WHERE uuid = '" + player.getAPIPlayer().getUUID() + "' AND skillName = '" + skill.getSkillName()+"';");
    }

    public int getSkillLevelUPPercentage(ISkill skill) {
        checkPlayer();
        ResultSet rs = new MySQLService().getResult("SELECT * FROM rpgSkills WHERE uuid = '" + player.getAPIPlayer().getUUID() + "' AND skillName = '" + skill.getSkillName() + "';");
        try {
            if(rs.next()) {
                return rs.getInt("percentage");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getSkillLevel(ISkill skill) {
        checkPlayer();
        ResultSet rs = new MySQLService().getResult("SELECT * FROM rpgSkills WHERE uuid = '" + player.getAPIPlayer().getUUID() + "' AND skillName = '" + skill.getSkillName() + "';");
        try {
            if(rs.next()) {
                return rs.getInt("level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void checkPlayer() {
        ResultSet rs = new MySQLService().getResult("SELECT * FROM rpgSkills WHERE uuid = '" + player.getAPIPlayer().getUUID() + "';");
        try {
            if(!rs.next()) {
                new SkillManager().getSkills().forEach(skill -> new MySQLService().getResult("INSERT INTO rpgSkills(skillName,level,percentage,uuid) VALUES ('" + skill.getSkillName() + "',1,0,'" + player.getAPIPlayer().getUUID() + "';"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        new MySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS rpgSkills(skillName TEXT, level INT, percentage INT, uuid TEXT);");
    }

}
