package com.ras.nbsurgu.telegram.commands;

import com.ras.nbsurgu.telegram.utils.Emoji;
import com.ras.nbsurgu.telegram.utils.KeyboardUtils;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class Start implements ICommand {

    @Override
    public SendMessage execute(final Update update) {
        return sendStartMessage(update.getMessage());
    }

    private SendMessage sendStartMessage(final Message message) {
        return new SendMessage()
                .enableMarkdown(true)
                .setReplyMarkup(KeyboardUtils.getReplyKeyboardMarkupForUser())
                .setText("Я очень рад, что могу пригодится тебе " + Emoji.WINKING_FACE +
                        "\nВыбирай команду из предложенного меню " + Emoji.BACKHAND +
                        "\nЕсли что-то не понятно, вводи \"/help\"")
                .setChatId(message.getChatId());
    }

}