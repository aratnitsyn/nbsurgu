package com.ras.nbsurgu.telegram.handlers;

import com.ras.nbsurgu.telegram.events.CommandEvents;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CommandHandlers implements Handlers {

    @Override
    public SendMessage execute(final Update update) {
        final Message message = update.getMessage();

        final String className = CommandEvents.getInstance().getClassName(message.getText());

        if (className == null) {
            return sendUnknownCommandMessage(message);
        }

        try {
            Class<?> cl = Class.forName(className);

            Constructor<?> constructor = cl.getConstructor();

            Object object = constructor.newInstance();

            Method method = cl.getMethod("execute", Update.class);

            return (SendMessage) method.invoke(object, update);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

}