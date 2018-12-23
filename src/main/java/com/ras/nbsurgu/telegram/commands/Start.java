package com.ras.nbsurgu.telegram.commands;

import com.ras.nbsurgu.telegram.utils.Emoji;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.ArrayList;

public class Start implements ICommand {

    private static final String NEWS_COMMAND = Emoji.NEWS + " Новости";
    private static final String WORKING_TIME =  Emoji.TIME + " Время работы";
    private static final String AUTHORIZATION = Emoji.AUTHORIZATION + " Авторизация";

    @Override
    public SendMessage execute(final Update update) {
        return sendStartMessage(update.getMessage());
    }

    private SendMessage sendStartMessage(final Message message) {
        return new SendMessage()
                .enableMarkdown(true)
                .setReplyMarkup(makeKeyboardForUser())
                .setText("Я очень рад, что могу пригодится тебе " + Emoji.WINKING_FACE +
                        "\nВыбирай команду из предложенного меню " + Emoji.BACKHAND +
                        "\nЕсли что-то не понятно, вводи <b>\"/help\"</b>")
                .setParseMode("HTML")
                .setChatId(message.getChatId());
    }

    private ReplyKeyboardMarkup makeKeyboardForUser() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        final List<KeyboardRow> keyboardRowList = new ArrayList<>();

        final KeyboardRow keyboardRowFirst = new KeyboardRow();
        keyboardRowFirst.add(NEWS_COMMAND);
        keyboardRowFirst.add(WORKING_TIME);

        final KeyboardRow keyboardRowSecond = new KeyboardRow();
        keyboardRowSecond.add(AUTHORIZATION);

        keyboardRowList.add(keyboardRowFirst);
        keyboardRowList.add(keyboardRowSecond);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;
    }

}