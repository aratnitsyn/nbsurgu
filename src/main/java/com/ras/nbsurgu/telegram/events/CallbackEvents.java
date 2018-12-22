package com.ras.nbsurgu.telegram.events;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

/**
 * Класс <code>CallbackEvents</code> является реализацией
 * интерфейса <code>Events</code>. Класс содержит весь список
 * событий, тип которых является "callback".
 */
public class CallbackEvents implements IEvents {

    private static final CallbackEvents instance = new CallbackEvents();

    private static final Map<List<String>, String> callbacks = new HashMap<>();

    public static CallbackEvents getInstance() {
        return instance;
    }

    @Override
    public void add(final String className, final List<String> value) {
        callbacks.put(value, className);
    }

    @Override
    public String getClassName(final String value) {
        if (!callbacks.isEmpty()) {
            for (List<String> callbackList : callbacks.keySet()) {
                if (callbackList.contains(value)) {
                    return callbacks.get(callbackList);
                }
            }
        }

        return null;
    }

}