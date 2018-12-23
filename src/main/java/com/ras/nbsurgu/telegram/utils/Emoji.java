package com.ras.nbsurgu.telegram.utils;

public enum Emoji {

    HMM('\uD83E', '\uDD14'),
    NEWS('\u2668', '\uFE0F'),
    TIME('\uD83D', '\uDD53'),
    BACKHAND('\uD83D', '\uDC47'),
    BUTTON_ONE('\u0031', '\u20E3'),
    BUTTON_TWO('\u0032', '\u20E3'),
    BUTTON_THREE('\u0033', '\u20E3'),
    WINKING_FACE('\uD83D', '\uDE09'),
    AUTHORIZATION('\uD83D', '\uDD11'),
    EXCLAMATION_MARK('\u2757', '\uFE0F');

    Character firstChar;
    Character secondChar;

    Emoji(Character firstChar, Character secondChar) {
        this.firstChar = firstChar;
        this.secondChar = secondChar;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        if (firstChar != null) {
            stringBuilder.append(firstChar);
        }

        if (secondChar != null) {
            stringBuilder.append(secondChar);
        }

        return stringBuilder.toString();
    }

}