package ru.ras.nbsurgu.telegram.schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorSchedulers {

    private static volatile ExecutorSchedulers instance;

    private static final List<ISchedulers> schedulers = new ArrayList<>();

    private ExecutorSchedulers() {
        schedulers.add(new BookSchedulers());
        schedulers.add(new NewsSchedulers());
    }

    public static ExecutorSchedulers getInstance() {
        if (instance == null) {
            synchronized (ExecutorSchedulers.class) {
                if (instance == null) {
                    instance = new ExecutorSchedulers();
                }
            }
        }

        return instance;
    }

    public void init() {
        for (ISchedulers scheduler : schedulers) {
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                    scheduler,
                    scheduler.getInitTime(),
                    scheduler.getDelayTime(),
                    TimeUnit.MILLISECONDS
            );
        }
    }

}