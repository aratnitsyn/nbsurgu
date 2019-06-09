package ru.ras.nbsurgu.telegram.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MessagesUtils {

    public static SendMessage getSendMessageWrong(final long chatId, final String text) {
        return new SendMessage().setChatId(chatId).setText(EmojiUtils.WRONG + " " + text);
    }

    public static SendMessage getSendMessageError(final long chatId) {
        return new SendMessage()
                .setChatId(chatId)
                .setText(EmojiUtils.WARNING + "Ошибка выполнения команды. Сообщите разработчику.")
                .enableMarkdown(true);
    }

}