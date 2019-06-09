package ru.ras.nbsurgu.telegram.keyboard;

public interface IKeyboardBuilder<T, Keyboard> {

    T create();
    T row();
    T endRow();
    T button(final String text);
    T button(final String text, final boolean contact);
    T button(final String text, final String callback);
    Keyboard build();

}