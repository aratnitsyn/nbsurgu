package com.ras.nbsurgu.telegram.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class News implements ICommand {

    @Override
    public SendMessage execute(Update update) {
        return new SendMessage()
                .enableMarkdown(true)
                .setText("Новости хочешь а? А их нет :)")
                .setChatId(update.getMessage().getChatId());
    }

}