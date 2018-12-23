package com.ras.nbsurgu.telegram.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ICommand {

    SendMessage execute(final Update update);

}