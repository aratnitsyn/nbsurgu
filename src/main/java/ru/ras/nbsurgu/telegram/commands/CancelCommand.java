package ru.ras.nbsurgu.telegram.commands;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import ru.ras.nbsurgu.telegram.database.entity.StateEntity;
import ru.ras.nbsurgu.telegram.database.service.StateService;
import ru.ras.nbsurgu.telegram.utils.EmojiUtils;
import ru.ras.nbsurgu.telegram.utils.MessagesUtils;
import ru.ras.nbsurgu.telegram.utils.StateUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CancelCommand implements ICommand {

    private static final String MESSAGE_CANCEL = "/cancel";

    private static final Set<String> callbacks = new HashSet<>();
    private static final Set<String> commands = new HashSet<>();
    private static final Set<Integer> states = new HashSet<>();

    public CancelCommand() {
        commands.add(MESSAGE_CANCEL);
    }

    @Override
    public @NotNull Set<String> getCallbacks() {
        return callbacks;
    }

    @Override
    public @NotNull Set<String> getCommands() {
        return commands;
    }

    @Override
    public @NotNull Set<Integer> getStates() {
        return states;
    }

    @Override
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final CallbackQuery callbackQuery) {
        return MessagesUtils.getSendMessageWrong(update.getMessage().getChatId(), "Не обработанная команда.");
    }

    @Override
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final StateEntity stateEntity) {
        return MessagesUtils.getSendMessageWrong(update.getMessage().getChatId(), "Не обработанная команда.");
    }

    @Override
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final Message message) {
        if (message.getText().equals(MESSAGE_CANCEL)) {
            return getSendMessageCancel(message);
        }

        return MessagesUtils.getSendMessageWrong(update.getMessage().getChatId(), "Не обработанная команда.");
    }

    private @NotNull SendMessage getSendMessageCancel(final Message message) {
        final long tgId = message.getFrom().getId();

        final Optional<StateEntity> optionalStateEntity = StateService.getInstance().read(tgId);

        StateService.getInstance().update(
                optionalStateEntity.orElse(new StateEntity(tgId, StateUtils.NONE, 0))
                        .setState(StateUtils.NONE).setTemporary(0)
        );

        return new SendMessage().setChatId(message.getChatId()).setText(EmojiUtils.OK + " Текущая команда отменена");
    }

}