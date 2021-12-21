package me.refluxo.serverlibrary.util.score.scoreboard;

import me.refluxo.serverlibrary.util.player.PlayerAPI;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import xyz.plocki.asyncthread.AsyncThread;

import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("unused")
public class ScoreboardAPI {

    private final PlayerAPI player;
    private static HashMap<PlayerAPI, Runnable> runnables;
    private static HashMap<PlayerAPI, Scoreboard> boards;

    public ScoreboardAPI(PlayerAPI playerAPI) {
        player = playerAPI;
    }

    public void saveRunnable(Runnable runnable) {
        runnables.put(player, runnable);
    }

    public void saveScoreboard(Scoreboard board) {
        boards.put(player, board);
    }

    public Scoreboard getScoreboard() {
        return boards.getOrDefault(player, Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard());
    }

    public static void init() {
        runnables = new HashMap<>();
        boards = new HashMap<>();
        new AsyncThread(() -> runnables.forEach((playerAPI, runnable) -> runnable.run())).scheduleAsyncTask(0, 20);
    }

}
