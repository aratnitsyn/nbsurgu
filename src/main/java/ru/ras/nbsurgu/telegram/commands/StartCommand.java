package ru.ras.nbsurgu.telegram.commands;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import ru.ras.nbsurgu.telegram.database.entity.AuthorizationEntity;
import ru.ras.nbsurgu.telegram.database.entity.StateEntity;
import ru.ras.nbsurgu.telegram.database.entity.UserEntity;
import ru.ras.nbsurgu.telegram.database.service.AuthorizationService;
import ru.ras.nbsurgu.telegram.database.service.StateService;
import ru.ras.nbsurgu.telegram.utils.EmojiUtils;
import ru.ras.nbsurgu.telegram.utils.KeyboardUtils;
import ru.ras.nbsurgu.telegram.utils.MessagesUtils;
import ru.ras.nbsurgu.telegram.utils.StateUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class StartCommand implements ICommand{

    private static final String COMMAND_START = "/start";

    private static final Set<String> callbacks = new HashSet<>();
    private static final Set<String> commands = new HashSet<>();
    private static final Set<Integer> states = new HashSet<>();

    public StartCommand() {
        commands.add(COMMAND_START);
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
        return MessagesUtils.getSendMessageWrong(callbackQuery.getMessage().getChatId(), "Не обработанная команда.");
    }

    @Override
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final StateEntity stateEntity) {
        return MessagesUtils.getSendMessageWrong(update.getMessage().getChatId(), "Не обработанная команда.");
    }

    @Override
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final Message message) {
        if (message.getText().equals(COMMAND_START)) {
            return getSendMessageCommandStart(message);
        }

        return MessagesUtils.getSendMessageWrong(message.getChatId(), "Не обработанная команда.");
    }

    private @NotNull SendMessage getSendMessageCommandStart(final Message message) {
        final long tgId = message.getFrom().getId();

        final StringBuilder stringBuilder = new StringBuilder();
        final Optional<StateEntity> optionalStateEntity = StateService.getInstance().read(tgId);

        StateService.getInstance().update(optionalStateEntity.orElse(new StateEntity(tgId, StateUtils.NONE, 0)).clear());

        final Optional<AuthorizationEntity> optionalAuthorizationEntity = AuthorizationService.getInstance().read(tgId);

        if (optionalAuthorizationEntity.isPresent()) {
            final UserEntity userEntity = optionalAuthorizationEntity.get().getUserId();

            stringBuilder
                    .append("Здравствуйте, ").append(userEntity.getName()).append(" ").append(EmojiUtils.WAVING_HAND)
                    .append("\nЧем могу помочь?");

            return new SendMessage()
                    .setChatId(message.getChatId())
                    .setText(stringBuilder.toString())
                    .setReplyMarkup(KeyboardUtils.getReplyKeyboardMarkupForUser());
        }

        stringBuilder
                .append("Здравствуйте ").append(EmojiUtils.WAVING_HAND)
                .append("\nЯ очень рад, что могу пригодится Вам ").append(EmojiUtils.WINKING_FACE)
                .append("\nВыбирайте команду из предложенного меню ").append(EmojiUtils.BACKHAND)
                .append("\nЕсли что-то не понятно, вводите \"/help\"");

        return new SendMessage()
                .setChatId(message.getChatId())
                .setText(stringBuilder.toString())
                .setReplyMarkup(KeyboardUtils.getReplyKeyboardMarkupMain());
    }

}