package xyz.plocki.asyncthread;

import me.refluxo.bungee.Bungee;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AsyncThread {

    private final Runnable runnable;
    private static final List<ScheduledTask> tasks = new ArrayList<>();

    public AsyncThread(Runnable runnable) {
        this.runnable = runnable;
    }

    public void runAsync() {
        new Thread(this.runnable).run();
    }

    public void runAsyncTaskLater(long seconds) {
        Bungee.getProxyServer().getScheduler().schedule(Bungee.getProxyServer().getPluginManager().getPlugin("Bungee"), runnable, seconds, TimeUnit.SECONDS);
    }

    public ScheduledTask scheduleAsyncTask(long initDelay, long seconds) {
        ScheduledTask task = Bungee.getProxyServer().getScheduler().schedule(Bungee.getProxyServer().getPluginManager().getPlugin("Bungee"), runnable, initDelay, seconds, TimeUnit.SECONDS);
        tasks.add(task);
        return task;
    }

    public static void stopTasks() {
        tasks.forEach(ScheduledTask::cancel);
        tasks.clear();
    }

}