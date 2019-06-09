package ru.ras.nbsurgu.telegram.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.ArrayList;

public class KeyboardUtils {

    private static final String NEWS_COMMAND = EmojiUtils.NEWS + " Новости";
    private static final String WORKING_TIME =  EmojiUtils.TIME + " Режим работы";
    private static final String AUTHORIZATION = EmojiUtils.AUTHORIZATION + " Авторизация";
    private static final String REPEAT_PASSWORD = EmojiUtils.SOS + " Забыл пароль";
    private static final String FORMULAR = EmojiUtils.GRADUATION_CAP + " Формуляр";
    private static final String PROFILE = EmojiUtils.SILHOUETTE_MAN + " Профиль";

    public static ReplyKeyboardMarkup getReplyKeyboardMarkupMain() {
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
        keyboardRowSecond.add(REPEAT_PASSWORD);

        keyboardRowList.add(keyboardRowFirst);
        keyboardRowList.add(keyboardRowSecond);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;
    }

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
        keyboardRowSecond.add(FORMULAR);
        keyboardRowSecond.add(PROFILE);

        keyboardRowList.add(keyboardRowFirst);
        keyboardRowList.add(keyboardRowSecond);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;
    }

}