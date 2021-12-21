package me.refluxo.rpglibrary.util.player.skills;

import me.refluxo.rpglibrary.util.player.RPGPlayer;
import me.refluxo.rpglibrary.util.player.data.MySQLSkill;
import me.refluxo.serverlibrary.util.player.PlayerAPI;

public class Skill {

    private final ISkill skill;
    private final PlayerAPI player;
    private final RPGPlayer rpgPlayer;

    public Skill(ISkill skill, PlayerAPI player) {
        this.skill = skill;
        this.player = player;
        rpgPlayer = new RPGPlayer(player);
    }

    public void setSkillLevel(int level) {
        new MySQLSkill(player).setSkillLevel(skill, level);
    }

    public int getSkillLevel() {
        return new MySQLSkill(player).getSkillLevel(skill);
    }

    public void setLevelUPPercentage(int percentage) {
        new MySQLSkill(player).setSkillLevelUPPercentage(skill, percentage);
    }

    public int getLevelUPPercentage() {
        return new MySQLSkill(player).getSkillLevelUPPercentage(skill);
    }

    public boolean activateSkill() {
        if(rpgPlayer.getRPGPlayer().getPower() >= skill.getCostActivation()) {
            rpgPlayer.setPower(rpgPlayer.getRPGPlayer().getPower()-skill.getCostActivation());
            return true;
        }
        return false;
    }

}
