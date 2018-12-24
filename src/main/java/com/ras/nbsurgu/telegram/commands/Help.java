package com.ras.nbsurgu.telegram.commands;

import com.ras.nbsurgu.telegram.utils.Emoji;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class Help implements ICommand {

    @Override
    public SendMessage execute(final Update update) {
        return sendHelpMessage(update.getMessage());
    }

    private SendMessage sendHelpMessage(final Message message) {
        return new SendMessage()
                .enableMarkdown(true)
                .setText("Помни " + Emoji.EXCLAMATION_MARK +
                        "\nТы моможешь выбирать команды из меню, либо присылать текстовые.\n\n" +
                        "Смотри какие текстовые есть:\n" +
                        Emoji.BUTTON_ONE + " \"/news\" - получить список новостей.\n" +
                        Emoji.BUTTON_TWO + " \"/times\" - получить время работы по каждому отделу, а также контактные данные руководителей.\n" +
                        Emoji.BUTTON_THREE + " \"/auth\" - авторизоваться в системе как студент."
                )
                .setChatId(message.getChatId());
    }

}