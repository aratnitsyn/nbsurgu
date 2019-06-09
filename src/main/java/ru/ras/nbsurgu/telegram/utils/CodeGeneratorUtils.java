package ru.ras.nbsurgu.telegram.utils;

public class CodeGeneratorUtils {

    private static final int MIN_NUMBER = 1000;
    private static final int MAX_NUMBER = 5000;

    private CodeGeneratorUtils() {}

    public static int get() {
        return (int)(MIN_NUMBER + (Math.random() * (MAX_NUMBER - MIN_NUMBER)));
    }

}