package com.ras.nbsurgu.telegram.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class WorkTime implements ICommand {

    @Override
    public SendMessage execute(final Update update) {
        return new SendMessage()
                .enableMarkdown(true)
                .setText("Время работы :O")
                .setChatId(update.getMessage().getChatId());
    }

}