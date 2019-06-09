package ru.ras.nbsurgu.telegram.utils;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class LibraryNewsUtils {

    private static volatile LibraryNewsUtils instance;

    private static List<Pair<String, String>> news = new ArrayList<>();

    private LibraryNewsUtils() {}

    public static LibraryNewsUtils getInstance() {
        if (instance == null) {
            synchronized (LibraryNewsUtils.class) {
                if (instance == null) {
                    instance = new LibraryNewsUtils();
                }
            }
        }

        return instance;
    }

    public void clear() {
        news.clear();
    }

    public void add(final String title, final String url) {
        news.add(
                new Pair(title, url)
        );
    }

    public List<Pair<String, String>> get() {
        return news;
    }

}