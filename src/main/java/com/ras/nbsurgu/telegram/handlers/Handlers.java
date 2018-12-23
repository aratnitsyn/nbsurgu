package com.ras.nbsurgu.telegram.handlers;

import com.ras.nbsurgu.telegram.utils.Emoji;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Handlers {

    SendMessage execute(final Update update);

    default SendMessage sendUnknownCommandMessage(final Message message) {
        return new SendMessage()
                .enableMarkdown(true)
                .setText(
                        "Не помню такой команды " + Emoji.HMM +
                        "\nВводи \"/help\" чтобы получить список команд."
                )
                .setChatId(message.getChatId());
    }

}