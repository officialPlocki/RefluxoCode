package me.refluxo.serverlibary.util.score.rank;

import java.util.HashMap;
import java.util.Map;

public class RankManager {

    private static final Map<String, Rank> ranks = new HashMap<>();

    public Rank getRank(String rankName) {
        return ranks.getOrDefault(rankName, null);
    }

    public void registerRank(String rankName, String prefix, String suffix) {
        ranks.put(rankName, new Rank() {

            @Override
            public String getPrefix() {
                return prefix;
            }

            @Override
            public String getSuffix() {
                return suffix;
            }

        });
    }

}
