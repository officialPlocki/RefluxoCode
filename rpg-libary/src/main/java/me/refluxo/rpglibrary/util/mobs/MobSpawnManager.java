package me.refluxo.rpglibrary.util.mobs;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import io.lumine.xikage.mythicmobs.MythicMobs;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MobSpawnManager {

    private final Player player;
    private final MobRegion region;

    public MobSpawnManager(Player player, MobRegion region) {
        this.player = player;
        this.region = region;
    }

    public void check() {
        Location location = player.getLocation();
        List<Entity> entities = Objects.requireNonNull(location.getWorld()).getNearbyEntities(location, 20, 7, 20).stream().toList();
        boolean spawn = true;
        if(entities.size() >= 7) {
            spawn = false;
        }
        if(spawn) {
            int spawnTimes = new Random().nextInt(4);
            MobRegionManager manager = new MobRegionManager(region);
            Random random = new Random();
            for(int i = 0; i < spawnTimes; i++) {
                MythicMobs.inst().getMobManager().spawnMob(manager.getAllowedMobsInRegion().get(random.nextInt(manager.getAllowedMobsInRegion().size())), getRandomLocation(location));
            }
        }
    }

    private Location getRandomLocation(Location origin) {
        Random r = new Random();
        double randomRadius = r.nextDouble() * (double) 20;
        double theta =  Math.toRadians(r.nextDouble() * 360);
        double phi = Math.toRadians(r.nextDouble() * 180 - 90);
        double x = randomRadius * Math.cos(theta) * Math.sin(phi);
        double z = randomRadius * Math.cos(phi);
        Location newLoc = origin.add(x, origin.getY(), z);
        while (!newLoc.getBlock().getType().isSolid() && !newLoc.getBlock().getType().isAir() && !newLoc.getBlock().isLiquid()) {
            if(newLoc.clone().subtract(0,1,0).getBlock().getType().isSolid()) {
                newLoc.add(0,1,0);
            }
        }
        return newLoc;
    }

}
