package ru.ras.nbsurgu.telegram.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardMarkupBuilder implements IKeyboardBuilder<InlineKeyboardMarkupBuilder, InlineKeyboardMarkup>  {

    private List<List<InlineKeyboardButton>> keyboardRowList = new ArrayList<>();
    private List<InlineKeyboardButton> keyboardRow = null;

    public InlineKeyboardMarkupBuilder create() {
        return new InlineKeyboardMarkupBuilder();
    }

    public InlineKeyboardMarkupBuilder row() {
        this.keyboardRow = new ArrayList<>();

        return this;
    }


    public InlineKeyboardMarkupBuilder endRow() {
        this.keyboardRowList.add(this.keyboardRow);

        this.keyboardRow = null;

        return this;
    }

    @Override
    public InlineKeyboardMarkupBuilder button(final String text) {
        return this;
    }

    @Override
    public InlineKeyboardMarkupBuilder button(final String text, final boolean contact) {
        return this;
    }

    @Override
    public InlineKeyboardMarkupBuilder button(final String text, final String callback) {
        this.keyboardRow.add(
                new InlineKeyboardButton().setText(text).setCallbackData(callback)
        );

        return this;
    }

    public InlineKeyboardMarkupBuilder button(final String text, final URL url) {
        this.keyboardRow.add(
                new InlineKeyboardButton().setText(text).setUrl(url.toString())
        );

        return this;
    }

    public InlineKeyboardMarkup build() {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.setKeyboard(keyboardRowList);

        return inlineKeyboardMarkup;
    }

}