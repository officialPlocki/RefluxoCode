package xyz.plocki.asyncthread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AsyncThread {

    public static ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
    private final Runnable run;

    public AsyncThread(Runnable runnable) {
        this.run = runnable;
    }

    public void runAsync() {
        ses.schedule(this.run, 1L, TimeUnit.MILLISECONDS);
        Thread thread = new Thread(this.run);
    }

    public void runAsyncTaskLater(long seconds) {
        ses.schedule(this.run, seconds, TimeUnit.SECONDS);
    }

    public ScheduledFuture<?> scheduleAsyncTask(long initDelay, long milli) {
        return ses.scheduleAtFixedRate(this.run, initDelay, milli, TimeUnit.MILLISECONDS);
    }

}