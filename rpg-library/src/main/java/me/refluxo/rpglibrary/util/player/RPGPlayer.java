package me.refluxo.rpglibrary.util.player;

import me.refluxo.rpglibrary.util.player.data.MySQLHealth;
import me.refluxo.rpglibrary.util.player.data.MySQLPower;
import me.refluxo.serverlibrary.util.player.PlayerAPI;

public class RPGPlayer {

    private PlayerAPI player;

    public RPGPlayer(PlayerAPI playerAPI) {
        player = playerAPI;
    }

    public void setHealth(int health) {
        new MySQLHealth(player).setHealth(health);
    }

    public void setMaxHealth(int maxHealth) {
        new MySQLHealth(player).setMaxHealth(maxHealth);
    }

    public void setPower(int power) {
        new MySQLPower(player).setPower(power);
    }

    public void setMaxPower(int maxPower) {
        new MySQLPower(player).setMaxPower(maxPower);
    }

    public IRPGPlayer getRPGPlayer() {
        return new IRPGPlayer() {
            @Override
            public int getPower() {
                return new MySQLPower(player).getPower();
            }

            @Override
            public int getMaxPower() {
                return new MySQLPower(player).getMaxPower();
            }

            @Override
            public int getHealth() {
                return new MySQLHealth(player).getHealth();
            }

            @Override
            public int getMaxHealth() {
                return new MySQLHealth(player).getMaxHealth();
            }
        };
    }

}
