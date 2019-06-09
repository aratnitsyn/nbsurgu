package ru.ras.nbsurgu.telegram.schedulers;

public interface ISchedulers extends Runnable {

    long getInitTime();
    long getDelayTime();

}