package ru.ras.nbsurgu.telegram.commands;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import ru.ras.nbsurgu.telegram.database.entity.StateEntity;

import java.util.Set;

public interface ICommand {

    @NotNull Set<String> getCallbacks();

    @NotNull Set<String> getCommands();

    @NotNull Set<Integer> getStates();

    @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final CallbackQuery callbackQuery);
    @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final StateEntity stateEntity);
    @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final Message message);

}