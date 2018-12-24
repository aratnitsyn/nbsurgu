package com.ras.nbsurgu.telegram.commands;

import com.ras.nbsurgu.telegram.utils.KeyboardUtils;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class Authorization implements ICommand {

    @Override
    public SendMessage execute(final Update update) {
        return chooseCommand(update.getMessage());
    }

    private SendMessage chooseCommand(final Message message) {
        String text = message.getText();

        if (text.equals(KeyboardUtils.LOGIN)) {
            return sendLoginMessage(message);
        } else if (text.equals(KeyboardUtils.REPEAT_PASSWORD)) {
            return sendRepeatPasswordMessage(message);
        } else if (text.equals(KeyboardUtils.BACK)) {
            return sendBackMessage(message);
        }

        return sendChooseCommandMessage(message);
    }

    private SendMessage sendLoginMessage(final Message message) {
        /*
            Установить состояние ввода "логин:пароль"
         */
        return new SendMessage()
                .enableMarkdown(true)
                .setText("Введи номер читательского билета и пароль.")
                .setChatId(message.getChatId());
    }

    private SendMessage sendRepeatPasswordMessage(final Message message) {
        /*
            Состояние ввода номера читательского
         */
        return new SendMessage()
                .enableMarkdown(true)
                .setText("Введи номер читательского билета.")
                .setChatId(message.getChatId());
    }

    private SendMessage sendChooseCommandMessage(final Message message) {
        return new SendMessage()
                .enableMarkdown(true)
                .setReplyMarkup(KeyboardUtils.getReplyKeyboardMarkupAuthorization())
                .setText("Хорошо, выбери команду.")
                .setChatId(message.getChatId());
    }

    private SendMessage sendBackMessage(final Message message) {
        /*

         */
        return new SendMessage()
                .enableMarkdown(true)
                .setReplyMarkup(KeyboardUtils.getReplyKeyboardMarkupForUser())
                .setText("Назад, так назад.")
                .setChatId(message.getChatId());
    }

}