package ru.ras.nbsurgu.telegram.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboardMarkupBuilder implements IKeyboardBuilder<ReplyKeyboardMarkupBuilder, ReplyKeyboardMarkup> {

    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    private List<KeyboardRow> keyboardRowList = null;

    private KeyboardRow keyboardRow = null;

    public ReplyKeyboardMarkupBuilder() {
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
    }

    @Override
    public ReplyKeyboardMarkupBuilder create() {
        this.keyboardRowList = new ArrayList<>();

        return this;
    }

    @Override
    public ReplyKeyboardMarkupBuilder row() {
        this.keyboardRow = new KeyboardRow();

        return this;
    }

    @Override
    public ReplyKeyboardMarkupBuilder endRow() {
        this.keyboardRowList.add(keyboardRow);

        this.keyboardRow = null;

        return this;
    }

    @Override
    public ReplyKeyboardMarkupBuilder button(final String text) {
        this.keyboardRow.add(text);

        return this;
    }

    @Override
    public ReplyKeyboardMarkupBuilder button(final String text, final boolean contact) {
        this.keyboardRow.add(
                new KeyboardButton()
                        .setText(text)
                        .setRequestContact(contact)
        );

        return this;
    }

    @Override
    public ReplyKeyboardMarkupBuilder button(final String text, final String callback) {
        return this;
    }

    @Override
    public ReplyKeyboardMarkup build() {
        return replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

}