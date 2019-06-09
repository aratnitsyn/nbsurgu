package ru.ras.nbsurgu.telegram.commands;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.ras.nbsurgu.telegram.Smtp;
import ru.ras.nbsurgu.telegram.database.entity.AuthorizationEntity;
import ru.ras.nbsurgu.telegram.database.entity.CodeEntity;
import ru.ras.nbsurgu.telegram.database.entity.StateEntity;
import ru.ras.nbsurgu.telegram.database.entity.UserEntity;
import ru.ras.nbsurgu.telegram.database.service.AuthorizationService;
import ru.ras.nbsurgu.telegram.database.service.CodeService;
import ru.ras.nbsurgu.telegram.database.service.StateService;
import ru.ras.nbsurgu.telegram.database.service.UserService;
import ru.ras.nbsurgu.telegram.keyboard.ReplyKeyboardMarkupBuilder;
import ru.ras.nbsurgu.telegram.utils.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ForgetPasswordCommand implements ICommand {

    private static final int STATE_WRITE_NUMBER_LIBRARY_CARD = StateUtils.get();
    private static final int STATE_CHOOSE_RECOVERY_METHOD = StateUtils.get();
    private static final int STATE_WRITE_CODE = StateUtils.get();
    private static final int STATE_WRITE_PASSWORD = StateUtils.get();

    private static final String MESSAGE_FORGET_PASSWORD = EmojiUtils.SOS + " Забыл пароль";
    private static final String MESSAGE_RECOVERY_METHOD_EMAIL = EmojiUtils.EMAIL + " E-Mail";
    private static final String MESSAGE_RECOVERY_METHOD_MOBILE_PHONE = EmojiUtils.MOBILE_PHONE + " Телефон";
    private static final String MESSAGE_BACK_TO_MAIN_MENU = EmojiUtils.BACK + " В главное меню";
    private static final String MESSAGE_CHOOSE_ANOTHER_RECOVERY_METHOD = EmojiUtils.BACK + " Выбрать другой метод";

    private static final Set<String> callbacks = new HashSet<>();
    private static final Set<String> commands = new HashSet<>();
    private static final Set<Integer> states = new HashSet<>();

    public ForgetPasswordCommand() {
        commands.add(MESSAGE_FORGET_PASSWORD);
        commands.add(MESSAGE_BACK_TO_MAIN_MENU);
        commands.add(MESSAGE_CHOOSE_ANOTHER_RECOVERY_METHOD);

        states.add(STATE_WRITE_NUMBER_LIBRARY_CARD);
        states.add(STATE_CHOOSE_RECOVERY_METHOD);
        states.add(STATE_WRITE_CODE);
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
        return MessagesUtils.getSendMessageWrong(callbackQuery.getMessage().getChatId(), "Не обработанная команда.");
    }

    @Override
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final StateEntity stateEntity) {
        final Message message = update.getMessage();

        final int state = stateEntity.getState();

        if (state == STATE_WRITE_NUMBER_LIBRARY_CARD) {
            return getSendMessageStateWriteNumberLibraryCard(message, stateEntity);
        }

        if (state == STATE_CHOOSE_RECOVERY_METHOD) {
            return getSendMessageStateChooseRecoveryMethod(message, stateEntity);
        }

        if (state == STATE_WRITE_CODE) {
            return getSendMessageStateWriteCode(message, stateEntity);
        }

        if (state == STATE_WRITE_PASSWORD) {
            return getSendMessageStateWritePassword(message, stateEntity);
        }

        return MessagesUtils.getSendMessageWrong(message.getChatId(), "Не обработанная команда.");
    }

    @Override
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final Message message) {
        final String text = message.getText();

        if (text.equals(MESSAGE_FORGET_PASSWORD)) {
            return getSendMessageForgetPassword(message);
        }

        if (text.equals(MESSAGE_CHOOSE_ANOTHER_RECOVERY_METHOD)) {
            return getSendMessageChooseRecoveryMethod(message);
        }

        if (text.equals(MESSAGE_BACK_TO_MAIN_MENU)) {
            return getSendMessageBackToMainMenu(message);
        }

        return MessagesUtils.getSendMessageWrong(message.getChatId(), "Не обработанная команда.");
    }

    private @NotNull SendMessage getSendMessageStateWriteNumberLibraryCard(final Message message, final StateEntity stateEntity) {
        final long chatId = message.getChatId();
        final String text = message.getText();

        if (text.isEmpty() || text.length() != 7) {
            return MessagesUtils.getSendMessageWrong(chatId, "Номер читательского билета должен состоять из 7 цифр.");
        }

        int numberLibraryCard;

        try {
            numberLibraryCard = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return MessagesUtils.getSendMessageWrong(chatId, "Номер читательского билета должен состоять из 7 цифр.");
        }

        final Optional<UserEntity> optionalUserEntity = UserService.getInstance().read(numberLibraryCard);

        if (optionalUserEntity.isPresent()) {
            stateEntity.setState(STATE_CHOOSE_RECOVERY_METHOD);
            stateEntity.setTemporary(numberLibraryCard);

            StateService.getInstance().update(stateEntity);

            return getSendMessageChooseRecoveryMethod(message);
        }

        return MessagesUtils.getSendMessageWrong(chatId, "Пользователь с таким читательским билетом не найден.");
    }

    private @NotNull SendMessage getSendMessageStateChooseRecoveryMethod(final Message message, final StateEntity stateEntity) {
        if (message.hasText() && message.getText().equals(MESSAGE_RECOVERY_METHOD_EMAIL)) {
            return getSendMessageRecoveryMethodEmail(message, stateEntity);
        }

        if (message.hasContact()) {
            return getSendMessageRecoveryMethodMobilePhone(message, stateEntity);
        }

        return MessagesUtils.getSendMessageWrong(message.getChatId(), "Выберите способ восстановления пароля " + EmojiUtils.BACKHAND);
    }

    private @NotNull SendMessage getSendMessageStateWriteCode(final Message message, final StateEntity stateEntity) {
        final long chatId = message.getChatId();
        final String text = message.getText();

        if (text.isEmpty() || text.length() < 4 || text.length() > 4) {
            return MessagesUtils.getSendMessageWrong(chatId, "Код подтверждения должен состоять из 4 цифр.");
        }

        int currentCode;

        try {
            currentCode = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return MessagesUtils.getSendMessageWrong(chatId, "Код подтверждения должен состоять из 4 цифр.");
        }

        final Optional<CodeEntity> optionalCodeEntity = CodeService.getInstance().read(message.getFrom().getId());

        if (optionalCodeEntity.isPresent()) {
            final CodeEntity codeEntity = optionalCodeEntity.get();

            final long liveTime = 15 * 60 * 1000; // 15 минут
            final long timeSending = codeEntity.getTimeSending().getTime();
            final long currentTime = new Date().getTime();

            if ((liveTime + timeSending) < currentTime) {
                CodeService.getInstance().delete(codeEntity);

                return MessagesUtils.getSendMessageWrong(chatId, "Время действия кода подтверждения истекло. Попробуйте ещё раз.");
            }

            final int code = codeEntity.getCode();

            if (currentCode != code) {
                return MessagesUtils.getSendMessageWrong(chatId, "Код подтверждения не верный.");
            }

            stateEntity.setState(STATE_WRITE_PASSWORD);

            StateService.getInstance().update(stateEntity);

            return new SendMessage()
                    .setChatId(chatId)
                    .setText("Введите новый пароль " + EmojiUtils.BACKHAND)
                    .setReplyMarkup(getReplyKeyboardMarkupChooseAnotherRecoveryMethod());
        }

        return MessagesUtils.getSendMessageError(chatId);
    }

    private @NotNull SendMessage getSendMessageStateWritePassword(final Message message, final StateEntity stateEntity) {
        final long chatId = message.getChatId();

        final String password = message.getText();

        if (password.isEmpty() || password.length() > 16) {
            return MessagesUtils.getSendMessageWrong(chatId, "Пароль должен быть от 1 до 16 символов.");
        }

        final Optional<UserEntity> optionalUserEntity = UserService.getInstance().read(stateEntity.getTemporary());

        if (optionalUserEntity.isPresent()) {
            CodeService.getInstance().delete(message.getFrom().getId());

            final UserEntity userEntity = optionalUserEntity.get();

            userEntity.setPassword(password);

            UserService.getInstance().update(userEntity);

            stateEntity.clear();

            StateService.getInstance().update(stateEntity);

            return new SendMessage()
                    .setChatId(chatId)
                    .setText(EmojiUtils.OK + " Ваш пароль был изменен на \"" + password + "\"")
                    .setReplyMarkup(KeyboardUtils.getReplyKeyboardMarkupMain());
        }

        return MessagesUtils.getSendMessageError(chatId);
    }

    private @NotNull SendMessage getSendMessageForgetPassword(final Message message) {
        final long tgId = message.getFrom().getId();
        final long chatId = message.getChatId();

        final Optional<AuthorizationEntity> optionalAuthorizationEntity = AuthorizationService.getInstance().read(tgId);

        if (optionalAuthorizationEntity.isPresent()) {
            return MessagesUtils.getSendMessageWrong(chatId, "Эта команда не доступна авторизованным пользователям.");
        }

        final Optional<StateEntity> optionalStateEntity = StateService.getInstance().read(tgId);

        StateService.getInstance().update(
                optionalStateEntity.orElse(new StateEntity(tgId, STATE_WRITE_NUMBER_LIBRARY_CARD, 0))
                .setState(STATE_WRITE_NUMBER_LIBRARY_CARD).setTemporary(0)
        );

        return new SendMessage()
                .setChatId(chatId)
                .setText("Введите номер читательского билета " + EmojiUtils.BACKHAND)
                .setReplyMarkup(KeyboardUtils.getReplyKeyboardMarkupMain());
    }

    private @NotNull SendMessage getSendMessageChooseRecoveryMethod(final Message message) {
        final long chatId = message.getChatId();

        final Optional<StateEntity> optionalStateEntity = StateService.getInstance().read(message.getFrom().getId());

        if (optionalStateEntity.isPresent()) {
            final StateEntity stateEntity = optionalStateEntity.get();
            final int state = stateEntity.getState();

            if (state != STATE_CHOOSE_RECOVERY_METHOD && state != STATE_WRITE_CODE && state != STATE_WRITE_PASSWORD) {
                return MessagesUtils.getSendMessageWrong(chatId, "В данный момент команда не активна.");
            }

            stateEntity.setState(STATE_CHOOSE_RECOVERY_METHOD);

            StateService.getInstance().update(stateEntity);

            return new SendMessage()
                    .setChatId(chatId)
                    .setText("Выберите способ восстановления пароля:\n" +
                            EmojiUtils.BUTTON_ONE + " E-Mail - на почту придет код подтверждения для смены пароля. " +
                            "После ввода кода подтверждения, Вы сможете ввести новый пароль.\n" +
                            EmojiUtils.BUTTON_TWO + " Телефон - приложение отправит боту Ваш текущий номер телефона. " +
                            "Если он будет соответствовать номеру указанному при получении читательского билета, " +
                            "то Вы сможете ввести новый пароль.")
                    .setReplyMarkup(getReplyKeyboardMarkupRecoveryMethod());
        }

        return MessagesUtils.getSendMessageError(chatId);
    }

    private @NotNull SendMessage getSendMessageRecoveryMethodEmail(final Message message, final StateEntity stateEntity) {
        final long tgId = message.getFrom().getId();
        final long chatId = message.getChatId();

        final Optional<UserEntity> optionalUserEntity = UserService.getInstance().read(stateEntity.getTemporary());

        if (optionalUserEntity.isPresent()) {
            final Optional<CodeEntity> optionalCodeEntity = CodeService.getInstance().read(tgId);

            CodeEntity codeEntity;

            final int code = CodeGeneratorUtils.get();

            if (optionalCodeEntity.isPresent()) {
                codeEntity = optionalCodeEntity.get();

                final long cooldown = 2 * 60 * 1000; // 2 минуты
                final long timeSending = codeEntity.getTimeSending().getTime();
                final long currentTime = new Date().getTime();

                if ((timeSending + cooldown) >= currentTime) {
                    return new SendMessage()
                            .setChatId(chatId)
                            .setText("Код подтверждения можно отправлять на почту 1 раз в 2 минуты." +
                                    " Введите уже ранее отправленный код " + EmojiUtils.BACKHAND)
                            .setReplyMarkup(getReplyKeyboardMarkupChooseAnotherRecoveryMethod());
                }

                codeEntity.setCode(code);
                codeEntity.setTimeSending(new Date());
            } else {
                codeEntity = new CodeEntity(tgId, code, new Date());
            }

            CodeService.getInstance().update(codeEntity);

            final UserEntity userEntity = optionalUserEntity.get();

            Smtp.getInstance().send(userEntity.getEmail(), code);

            stateEntity.setState(STATE_WRITE_CODE);

            StateService.getInstance().update(stateEntity);

            return new SendMessage()
                    .setChatId(chatId)
                    .setText("На почту был отправлен код подтверждения. Введите его " + EmojiUtils.BACKHAND)
                    .setReplyMarkup(getReplyKeyboardMarkupChooseAnotherRecoveryMethod());
        }

        return MessagesUtils.getSendMessageError(chatId);
    }

    private @NotNull SendMessage getSendMessageRecoveryMethodMobilePhone(final Message message, final StateEntity stateEntity) {
        final long chatId = message.getChatId();

        String phoneNumber = message.getContact().getPhoneNumber();

        phoneNumber = phoneNumber.contains("+") ? phoneNumber : ("+" + phoneNumber);

        final Optional<UserEntity> optionalUserEntity = UserService.getInstance().read(phoneNumber);

        if (optionalUserEntity.isPresent()) {
            stateEntity.setState(STATE_WRITE_PASSWORD);

            StateService.getInstance().update(stateEntity);

            return new SendMessage()
                    .setChatId(chatId)
                    .setText("Введите новый пароль " + EmojiUtils.BACKHAND)
                    .setReplyMarkup(getReplyKeyboardMarkupChooseAnotherRecoveryMethod());
        }

        return MessagesUtils.getSendMessageWrong(chatId, "Пользователь с таким номером телефона не найден.");
    }

    private @NotNull SendMessage getSendMessageBackToMainMenu(final Message message) {
        final long tgId = message.getFrom().getId();

        final Optional<StateEntity> stateEntity = StateService.getInstance().read(tgId);

        StateService.getInstance().update(
                stateEntity.orElse(new StateEntity(tgId, 0, 0)).clear()
        );

        return new SendMessage()
                .setChatId(message.getChatId())
                .setText(MESSAGE_BACK_TO_MAIN_MENU)
                .setReplyMarkup(KeyboardUtils.getReplyKeyboardMarkupMain());
    }

    private @NotNull ReplyKeyboardMarkup getReplyKeyboardMarkupRecoveryMethod() {
        return new ReplyKeyboardMarkupBuilder().create()
                .row()
                .button(MESSAGE_RECOVERY_METHOD_EMAIL)
                .button(MESSAGE_RECOVERY_METHOD_MOBILE_PHONE, true)
                .endRow()
                .row()
                .button(MESSAGE_BACK_TO_MAIN_MENU)
                .endRow()
                .build();
    }

    private @NotNull ReplyKeyboardMarkup getReplyKeyboardMarkupChooseAnotherRecoveryMethod() {
        return new ReplyKeyboardMarkupBuilder().create()
                .row()
                .button(MESSAGE_CHOOSE_ANOTHER_RECOVERY_METHOD)
                .endRow()
                .build();
    }

}