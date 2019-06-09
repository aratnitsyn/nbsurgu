package ru.ras.nbsurgu.telegram.commands;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ras.nbsurgu.telegram.database.entity.AuthorizationEntity;
import ru.ras.nbsurgu.telegram.database.entity.StateEntity;
import ru.ras.nbsurgu.telegram.database.entity.UserEntity;
import ru.ras.nbsurgu.telegram.database.service.AuthorizationService;
import ru.ras.nbsurgu.telegram.database.service.StateService;
import ru.ras.nbsurgu.telegram.database.service.UserService;
import ru.ras.nbsurgu.telegram.keyboard.InlineKeyboardMarkupBuilder;
import ru.ras.nbsurgu.telegram.utils.EmojiUtils;
import ru.ras.nbsurgu.telegram.utils.KeyboardUtils;
import ru.ras.nbsurgu.telegram.utils.MessagesUtils;
import ru.ras.nbsurgu.telegram.utils.StateUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ProfileCommand implements ICommand {

    private static final int STATE_WRITE_PASSWORD = StateUtils.get();

    private static final String CALLBACK_EXIT = "callback_profile_exit";
    private static final String CALLBACK_CHANGE_PASSWORD = "callback_profile_change_password";

    private static final String MESSAGE_PROFILE = EmojiUtils.SILHOUETTE_MAN + " Профиль";

    private static final String NAME_BUTTON_EXIT = EmojiUtils.ARROW_END + " Выйти";
    private static final String NAME_BUTTON_CHANGE_PASSWORD = EmojiUtils.REPEAT + " Изменить пароль";

    private static final Set<String> callbacks = new HashSet<>();
    private static final Set<String> commands = new HashSet<>();
    private static final Set<Integer> states = new HashSet<>();

    public ProfileCommand() {
        callbacks.add(CALLBACK_EXIT);
        callbacks.add(CALLBACK_CHANGE_PASSWORD);

        commands.add(MESSAGE_PROFILE);

        states.add(STATE_WRITE_PASSWORD);
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
        final String data = callbackQuery.getData();

        if (data.equals(CALLBACK_EXIT)) {
            return getSendMessageCallbackExit(absSender, callbackQuery);
        }

        if (data.equals(CALLBACK_CHANGE_PASSWORD)) {
            return getSendMessageCallbackChangePassword(callbackQuery);
        }

        return MessagesUtils.getSendMessageWrong(update.getMessage().getChatId(), "Не обработанная команда.");
    }

    @Override
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final StateEntity stateEntity) {
        if (stateEntity.getState() == STATE_WRITE_PASSWORD) {
            return getSendMessageStateWritePassword(update.getMessage(), stateEntity);
        }

        return MessagesUtils.getSendMessageWrong(update.getMessage().getChatId(), "Не обработанная команда.");
    }

    @Override
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final Message message) {
        if (message.getText().equals(MESSAGE_PROFILE)) {
            return getSendMessageProfile(message);
        }

        return MessagesUtils.getSendMessageWrong(message.getChatId(), "Не обработанная команда.");
    }


    private @NotNull BotApiMethod getSendMessageCallbackExit(final AbsSender absSender, final CallbackQuery callbackQuery) {
        final Optional<AuthorizationEntity> optionalAuthorizationEntity = AuthorizationService.getInstance().read(callbackQuery.getFrom().getId());

        if (optionalAuthorizationEntity.isPresent()) {
            AuthorizationService.getInstance().delete(optionalAuthorizationEntity.get());

            try {
                absSender.execute(
                        new AnswerCallbackQuery().setCallbackQueryId(callbackQuery.getId())
                );
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            return new SendMessage()
                    .setChatId(callbackQuery.getMessage().getChatId())
                    .setText(EmojiUtils.OK + " Вы вышли.")
                    .setReplyMarkup(KeyboardUtils.getReplyKeyboardMarkupMain());
        }

        return new AnswerCallbackQuery()
                .setCallbackQueryId(callbackQuery.getId())
                .setText("Эта команда доступна только авторизованным пользователям.")
                .setShowAlert(true);
    }

    private @NotNull BotApiMethod getSendMessageCallbackChangePassword(final CallbackQuery callbackQuery) {
        final long tgId = callbackQuery.getFrom().getId();

        final Optional<AuthorizationEntity> optionalAuthorizationEntity = AuthorizationService.getInstance().read(tgId);

        if (optionalAuthorizationEntity.isPresent()) {
            final Optional<StateEntity> optionalStateEntity = StateService.getInstance().read(tgId);

            StateService.getInstance().update(
                    optionalStateEntity.orElse(new StateEntity(tgId, STATE_WRITE_PASSWORD, 0))
                            .setState(STATE_WRITE_PASSWORD).setTemporary(0)
            );

            return new EditMessageText()
                    .setChatId(callbackQuery.getMessage().getChatId())
                    .setMessageId(callbackQuery.getMessage().getMessageId())
                    .setText("Введите новый пароль " + EmojiUtils.BACKHAND);
        }

        return new AnswerCallbackQuery()
                .setCallbackQueryId(callbackQuery.getId())
                .setText("Эта команда доступна только авторизованным пользователям.")
                .setShowAlert(true);
    }

    private @NotNull SendMessage getSendMessageStateWritePassword(final Message message, final StateEntity stateEntity) {
        final long chatId = message.getChatId();

        final String password = message.getText();

        if (password.isEmpty() || password.length() > 16) {
            return MessagesUtils.getSendMessageWrong(chatId, "Пароль должен быть от 1 до 16 символов.");
        }

        final Optional<AuthorizationEntity> optionalAuthorizationEntity = AuthorizationService.getInstance().read(message.getFrom().getId());

        if (optionalAuthorizationEntity.isPresent()) {
            final AuthorizationEntity authorizationEntity = optionalAuthorizationEntity.get();

            final UserEntity userEntity = authorizationEntity.getUserId();

            userEntity.setPassword(password);

            UserService.getInstance().update(userEntity);

            stateEntity.clear();

            StateService.getInstance().update(stateEntity);

            return new SendMessage().setChatId(chatId).setText(EmojiUtils.OK + " Ваш пароль был изменен на \"" + password + "\"");
        }

        return MessagesUtils.getSendMessageWrong(chatId, "Эта команда доступна только авторизованным пользователям.");
    }

    private @NotNull SendMessage getSendMessageProfile(final Message message) {
        final long chatId = message.getChatId();

        final Optional<AuthorizationEntity> optionalAuthorizationEntity = AuthorizationService.getInstance().read(message.getFrom().getId());

        if (optionalAuthorizationEntity.isPresent()) {
            return new SendMessage().setChatId(chatId).setText("Выберите команду " + EmojiUtils.BACKHAND).setReplyMarkup(getInlineKeyboardMarkupProfile());
        }

        return MessagesUtils.getSendMessageWrong(chatId, "Эта команда доступна только авторизованным пользователям.");
    }

    private @NotNull InlineKeyboardMarkup getInlineKeyboardMarkupProfile () {
        return new InlineKeyboardMarkupBuilder()
                .row()
                .button(NAME_BUTTON_EXIT, CALLBACK_EXIT)
                .button(NAME_BUTTON_CHANGE_PASSWORD, CALLBACK_CHANGE_PASSWORD)
                .endRow()
                .build();
    }

}