package xyz.plocki.asyncthread;

import me.refluxo.serverlibrary.ServerLibrary;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public class AsyncThread {

    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(20000);
    private final Runnable runnable;
    private static final List<Integer> tasks = new ArrayList<>();

    public AsyncThread(Runnable runnable) {
        this.runnable = runnable;
    }

    public void runAsync() {
        new Thread(this.runnable).run();
    }

    public void runAsyncTaskLater(long seconds) {
        Bukkit.getScheduler().runTaskLater(ServerLibrary.getPlugin(), runnable, seconds*20);
    }

    public int scheduleAsyncTask(long initDelay, long seconds) {
        int t = Bukkit.getScheduler().scheduleSyncRepeatingTask(ServerLibrary.getPlugin(), runnable, initDelay, seconds*20);
        tasks.add(t);
        return t;
    }

    public static void stopTasks() {
        tasks.forEach(task -> {
            Bukkit.getScheduler().cancelTask(task);
        });
        tasks.clear();
    }
}