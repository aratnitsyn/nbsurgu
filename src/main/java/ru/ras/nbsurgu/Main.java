package ru.ras.nbsurgu;

import ru.ras.nbsurgu.telegram.Smtp;
import ru.ras.nbsurgu.telegram.Telegram;
import ru.ras.nbsurgu.telegram.schedulers.ExecutorSchedulers;

public class Main {

    public static void main(String[] args) {
        init();

        new Telegram().start();
    }

    private static void init() {
        smtp();

        schedulers();
    }

    private static void smtp() {
        Smtp.getInstance().init();
    }

    private static void schedulers() {
        ExecutorSchedulers.getInstance().init();
    }
}