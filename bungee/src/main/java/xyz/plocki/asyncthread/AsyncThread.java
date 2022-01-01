package xyz.plocki.asyncthread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AsyncThread {

    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(20000);
    private final Runnable runnable;
    private static final List<ScheduledFuture<?>> tasks = new ArrayList<>();

    public AsyncThread(Runnable runnable) {
        this.runnable = runnable;
    }

    public void runAsync() {
        scheduler.schedule(this.runnable, 1L, TimeUnit.MILLISECONDS);
        Thread thread = new Thread(this.runnable);
    }

    public void runAsyncTaskLater(long seconds) {
        scheduler.schedule(this.runnable, seconds, TimeUnit.SECONDS);
    }

    public ScheduledFuture<?> scheduleAsyncTask(long initDelay, long seconds) {
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(this.runnable, initDelay, seconds, TimeUnit.SECONDS);
        tasks.add(future);
        return future;
    }

    public static void stopTasks() {
        tasks.forEach(scheduledFuture -> {
            scheduledFuture.cancel(true);
        });
        tasks.clear();
    }

}