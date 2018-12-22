package com.ras.nbsurgu.telegram.events;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

/**
 * Класс <code>CommandEvents</code> является реализацией
 * интерфейса <code>Events</code>. Класс содержит весь список
 * событий, тип которых является "команда".
 */
public class CommandEvents implements IEvents {

    private static final CommandEvents instance = new CommandEvents();

    private static final Map<List<String>, String> commands = new HashMap<>();

    public static CommandEvents getInstance() {
        return instance;
    }

    @Override
    public void add(final String className, final List<String> value) {
        commands.put(value, className);
    }

    @Override
    public String getClassName(final String value) {
        if (!commands.isEmpty()) {
            for (List<String> commandList : commands.keySet()) {
                if (commandList.contains(value)) {
                    return commands.get(commandList);
                }
            }
        }

        return null;
    }

}