package com.ras.nbsurgu.telegram.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.ArrayList;

public class KeyboardUtils {

    public static final String NEWS_COMMAND = Emoji.NEWS + " Новости";
    public static final String WORKING_TIME =  Emoji.TIME + " Время работы";
    public static final String AUTHORIZATION = Emoji.AUTHORIZATION + " Авторизация";
    public static final String LOGIN = Emoji.INPUT_NUMBERS + " Войти";
    public static final String REPEAT_PASSWORD = Emoji.REPEAT + " Забыл пароль";
    public static final String BACK = Emoji.BACK + " Назад";

    public static ReplyKeyboardMarkup getReplyKeyboardMarkupForUser() {
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

    public static ReplyKeyboardMarkup getReplyKeyboardMarkupAuthorization() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        final List<KeyboardRow> keyboardRowList = new ArrayList<>();

        final KeyboardRow keyboardRowFirst = new KeyboardRow();
        keyboardRowFirst.add(LOGIN);
        keyboardRowFirst.add(REPEAT_PASSWORD);

        final KeyboardRow keyboardRowSecond = new KeyboardRow();
        keyboardRowSecond.add(BACK);

        keyboardRowList.add(keyboardRowFirst);
        keyboardRowList.add(keyboardRowSecond);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;
    }

}