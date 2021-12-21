package me.refluxo.rpglibrary.util.player.skills;

import java.util.ArrayList;
import java.util.List;

public class SkillManager {

    private static final List<ISkill> skills = new ArrayList<>();

    public ISkill createSkill(String skillName, int activateCost) {
        return new ISkill() {
            @Override
            public String getSkillName() {
                return skillName;
            }

            @Override
            public int getCostActivation() {
                return activateCost;
            }
        };

    }

    public void registerSkill(ISkill skill) {
        skills.add(skill);
    }

    public List<ISkill> getSkills() {
        return skills;
    }

}
