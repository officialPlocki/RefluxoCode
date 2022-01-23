package me.refluxo.rpglibrary.util.mobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MobRegionManager {

    public static HashMap<String, MobRegion> regionMobs = new HashMap<>();

    private final MobRegion region;

    public MobRegionManager(MobRegion region) {
        this.region = region;
    }

    public void addMobToRegion(String internalName) {
        regionMobs.put(internalName, region);
    }

    public void removeMobFromRegion(String internalName) {
        regionMobs.remove(internalName);
    }

    public boolean mobIsAllowedInRegion(String internalName) {
        return regionMobs.containsKey(internalName);
    }

    public void removeAllMobsFromRegion() {
        regionMobs.clear();
    }

    public List<String> getAllowedMobsInRegion() {
        List<String> list = new ArrayList<>();
        regionMobs.forEach((internalName, mobRegion) -> {
            if(mobRegion.equals(region)) {
                list.add(internalName);
            }
        });
        return list;
    }

}
