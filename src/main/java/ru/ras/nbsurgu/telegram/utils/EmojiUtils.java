package ru.ras.nbsurgu.telegram.utils;

public enum EmojiUtils {

    OK('\u2705', '\u0000'),
    SOS('\uD83C', '\uDD98'),
    TAB('\u0009', '\u0009'),
    HMM('\uD83E', '\uDD14'),
    DOOR('\uD83D', '\uDEAA'),
    BACK('\u21A9', '\uFE0F'),
    NEWS('\u2668', '\uFE0F'),
    TIME('\uD83D', '\uDD53'),
    BOOKS('\uD83D', '\uDCDA'),
    EMAIL('\uD83D', '\uDCE7'),
    WRONG('\uD83D', '\uDEAB'),
    REPEAT('\uD83D', '\uDD01'),
    ORDERS('\uD83D', '\uDCDD'),
    WARNING('\u26A0', '\uFE0F'),
    BACKHAND('\uD83D', '\uDC47'),
    CALENDAR('\uD83D', '\uDDD3'),
    ARROW_END('\uD83D', '\uDD1A'),
    TELEPHONE('\u260E', '\uFE0F'),
    BUTTON_ONE('\u0031', '\u20E3'),
    BUTTON_TWO('\u0032', '\u20E3'),
    WAVING_HAND('\uD83D', '\uDC4B'),
    WINKING_FACE('\uD83D', '\uDE09'),
    MOBILE_PHONE('\uD83D', '\uDCF1'),
    AUTHORIZATION('\uD83D', '\uDD11'),
    SILHOUETTE_MAN('\uD83D', '\uDC64'),
    GRADUATION_CAP('\uD83C', '\uDF93'),
    EXCLAMATION_MARK('\u2757', '\uFE0F');

    Character firstChar;
    Character secondChar;

    EmojiUtils(Character firstChar, Character secondChar) {
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