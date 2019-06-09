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
import ru.ras.nbsurgu.telegram.database.service.UserService;
import ru.ras.nbsurgu.telegram.utils.EmojiUtils;
import ru.ras.nbsurgu.telegram.utils.KeyboardUtils;
import ru.ras.nbsurgu.telegram.utils.MessagesUtils;
import ru.ras.nbsurgu.telegram.utils.StateUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizationCommand implements ICommand {

    private static final int STATE_AUTHORIZATION = StateUtils.get();

    private static final String MESSAGE_AUTHORIZATION = EmojiUtils.AUTHORIZATION + " Авторизация";

    private static final String REGEX_EXPRESSION_AUTHORIZATION = "^([0-9]+)\\s+([0-9a-zA-Z]+)$";

    private static final Pattern patternAuthorization = Pattern.compile(REGEX_EXPRESSION_AUTHORIZATION);

    private static final Set<String> callbacks = new HashSet<>();
    private static final Set<String> commands = new HashSet<>();
    private static final Set<Integer> states = new HashSet<>();

    public AuthorizationCommand() {
        commands.add(MESSAGE_AUTHORIZATION);

        states.add(STATE_AUTHORIZATION);
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
        if (stateEntity.getState() == STATE_AUTHORIZATION) {
            return getSendMessageStateAuthorization(update.getMessage(), stateEntity);
        }

        return MessagesUtils.getSendMessageWrong(update.getMessage().getChatId(), "Не обработанная команда.");
    }

    @Override
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final Message message) {
        if (message.getText().equals(MESSAGE_AUTHORIZATION)) {
            return getSendMessageAuthorization(message);
        }

        return MessagesUtils.getSendMessageWrong(message.getChatId(), "Не обработанная команда.");
    }

    private @NotNull SendMessage getSendMessageStateAuthorization(final Message message, final StateEntity stateEntity) {
        final long chatId = message.getChatId();
        final long tgId = message.getFrom().getId();
        final String text = message.getText().trim();

        long numberLibraryCard;
        String password;

        final Matcher matcher = patternAuthorization.matcher(text);

        if (matcher.find() && matcher.groupCount() == 2) {
            numberLibraryCard = Long.parseLong(matcher.group(1));
            password = matcher.group(2);
        } else {
            return MessagesUtils.getSendMessageWrong(chatId, "Данные введены не корректно.");
        }

        final Optional<UserEntity> optionalUserEntity = UserService.getInstance().read(numberLibraryCard);

        if (optionalUserEntity.isPresent()) {
            final UserEntity userEntity = optionalUserEntity.get();

            if (!userEntity.getPassword().equals(password)) {
                return MessagesUtils.getSendMessageWrong(chatId, "Неверный пароль.");
            }

            AuthorizationService.getInstance().create(new AuthorizationEntity(tgId, userEntity));

            StateService.getInstance().update(stateEntity.clear());

            return new SendMessage()
                    .setChatId(chatId)
                    .setText("Здравствуйте, " + userEntity.getName()
                            + " " + EmojiUtils.WAVING_HAND + "\nОчень приятно познакомиться.")
                    .setReplyMarkup(KeyboardUtils.getReplyKeyboardMarkupForUser());
        }

        return MessagesUtils.getSendMessageWrong(chatId, "Пользователь с таким читательским билетом не найден.");
    }

    private @NotNull SendMessage getSendMessageAuthorization(final Message message) {
        final long tgId = message.getFrom().getId();
        final long chatId = message.getChatId();

        final Optional<AuthorizationEntity> optionalAuthorizationEntity = AuthorizationService.getInstance().read(tgId);

        if (optionalAuthorizationEntity.isPresent()) {
            return MessagesUtils.getSendMessageWrong(
                    chatId,
                    "Эта команда не доступна авторизованным пользователям.");
        }

        final Optional<StateEntity> optionalStateEntity = StateService.getInstance().read(tgId);

        StateService.getInstance().update(
                optionalStateEntity.orElse(new StateEntity(tgId, STATE_AUTHORIZATION, 0))
                        .setState(STATE_AUTHORIZATION).setTemporary(0)
        );

        return new SendMessage()
                .setChatId(chatId)
                .setText("Введите номер читательского билета и пароль через пробел.\nНапример: 12345 QwErTy\n" +
                        "Примечание: пароль чувствителен к регистру.")
                .setReplyMarkup(KeyboardUtils.getReplyKeyboardMarkupMain());
    }

}