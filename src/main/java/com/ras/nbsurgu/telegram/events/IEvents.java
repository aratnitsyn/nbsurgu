package com.ras.nbsurgu.telegram.events;

import java.util.List;

/**
 * Интерфейс <code>IEvents</code> представляет из себя шаблон для
 * реализации разных видов событий.
 * Реализацию событий можно увидеть в пакете <code>events</code>.
 */
public interface IEvents {

    void add(final String className, final List<String> value);
    String getClassName(final String value);

}