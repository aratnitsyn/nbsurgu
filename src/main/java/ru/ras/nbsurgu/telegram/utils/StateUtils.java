package ru.ras.nbsurgu.telegram.utils;

public class StateUtils {

    public static int NONE = 0;

    private static int counter = 0;

    private StateUtils() {}

    public static int get() {
        return ++counter;
    }

}