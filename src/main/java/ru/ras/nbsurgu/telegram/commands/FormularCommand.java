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
import ru.ras.nbsurgu.telegram.database.entity.*;
import ru.ras.nbsurgu.telegram.database.service.AuthorizationService;
import ru.ras.nbsurgu.telegram.database.service.OrderBookService;
import ru.ras.nbsurgu.telegram.database.service.TakenBookService;
import ru.ras.nbsurgu.telegram.keyboard.InlineKeyboardMarkupBuilder;
import ru.ras.nbsurgu.telegram.utils.EmojiUtils;
import ru.ras.nbsurgu.telegram.utils.MessagesUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class FormularCommand implements ICommand {

    private static final String CALLBACK_MY_BOOKS = "callback_formular_my_books";
    private static final String CALLBACK_MY_ORDERS = "callback_formular_my_orders";
    private static final String CALLBACK_BACK = "callback_formular_back";

    private static final String MESSAGE_FORMULAR = EmojiUtils.GRADUATION_CAP + " Формуляр";

    private static final String NAME_BUTTON_MY_BOOKS = EmojiUtils.BOOKS + " Мои книги";
    private static final String NAME_BUTTON_MY_ORDERS = EmojiUtils.ORDERS + " Заказы";
    private static final String NAME_BUTTON_BACK = EmojiUtils.BACK + " Назад";

    private static final Set<String> callbacks = new HashSet<>();
    private static final Set<String> commands = new HashSet<>();
    private static final Set<Integer> states = new HashSet<>();

    public FormularCommand() {
        callbacks.add(CALLBACK_MY_BOOKS);
        callbacks.add(CALLBACK_MY_ORDERS);
        callbacks.add(CALLBACK_BACK);

        commands.add(MESSAGE_FORMULAR);
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

        if (data.equals(CALLBACK_MY_BOOKS)) {
            return getSendMessageCallbackMyBooks(absSender, callbackQuery);
        }

        if (data.equals(CALLBACK_MY_ORDERS)) {
            return getSendMessageCallbackMyOrders(absSender, callbackQuery);
        }

        if (data.equals(CALLBACK_BACK)) {
            return getSendMessageFormular(absSender, callbackQuery);
        }

        return MessagesUtils.getSendMessageWrong(update.getMessage().getChatId(), "Не обработанная команда.");
    }

    @Override
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final StateEntity stateEntity) {
        return MessagesUtils.getSendMessageWrong(update.getMessage().getChatId(), "Не обработанная команда.");
    }

    @Override
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final Message message) {
        if (message.getText().equals(MESSAGE_FORMULAR)) {
            return getSendMessageFormular(message);
        }

        return MessagesUtils.getSendMessageWrong(update.getMessage().getChatId(), "Не обработанная команда.");
    }

    private @NotNull BotApiMethod getSendMessageCallbackMyBooks(final AbsSender absSender, final CallbackQuery callbackQuery) {
        final Optional<AuthorizationEntity> optionalAuthorizationEntity = AuthorizationService.getInstance().read(callbackQuery.getFrom().getId());

        if (optionalAuthorizationEntity.isPresent()) {
            final AuthorizationEntity authorizationEntity = optionalAuthorizationEntity.get();
            final UserEntity userEntity = authorizationEntity.getUserId();

            final List<TakenBookEntity> takenBookEntities = TakenBookService.getInstance().read(userEntity.getId());

            if (takenBookEntities.isEmpty()) {
                return new AnswerCallbackQuery()
                        .setCallbackQueryId(callbackQuery.getId())
                        .setShowAlert(true)
                        .setText("В данный момент у вас нету взятых книг.");
            }

            final StringBuilder stringBuilder = new StringBuilder();

            for (TakenBookEntity takenBook : takenBookEntities) {
                final CopyBookEntity copyBookEntity = takenBook.getCopyBookEntity();
                final BookEntity book = copyBookEntity.getBookEntity();
                final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                final String date = dateFormat.format(takenBook.getDateReturn());

                if (takenBook.getDateActually() == null) {
                    stringBuilder
                            .append("• ").append(book.getName()).append(" (").append(book.getAuthor()).append(") (до ")
                            .append(date).append(")\n");
                }
            }

            try {
                absSender.execute(
                        new AnswerCallbackQuery().setCallbackQueryId(callbackQuery.getId())
                );
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            return new EditMessageText()
                    .setChatId(callbackQuery.getMessage().getChatId())
                    .setMessageId(callbackQuery.getMessage().getMessageId())
                    .setText(stringBuilder.toString())
                    .setReplyMarkup(getInlineKeyboardMarkupBack());
        }

        return new AnswerCallbackQuery()
                .setCallbackQueryId(callbackQuery.getId())
                .setText("Данная команда доступна только авторизованным пользователям.")
                .setShowAlert(true);
    }

    private @NotNull BotApiMethod getSendMessageCallbackMyOrders(final AbsSender absSender, final CallbackQuery callbackQuery) {
        final Optional<AuthorizationEntity> optionalAuthorizationEntity = AuthorizationService.getInstance().read(callbackQuery.getFrom().getId());

        if (optionalAuthorizationEntity.isPresent()) {
            final AuthorizationEntity authorizationEntity = optionalAuthorizationEntity.get();
            final UserEntity userEntity = authorizationEntity.getUserId();

            final List<OrderBookEntity> orderBookEntities = OrderBookService.getInstance().read(userEntity.getId());

            if (orderBookEntities.isEmpty()) {
                return new AnswerCallbackQuery()
                        .setCallbackQueryId(callbackQuery.getId())
                        .setShowAlert(true)
                        .setText("В данный момент у вас нету заказанных книг.");
            }

            final StringBuilder stringBuilder = new StringBuilder();

            for (OrderBookEntity orderBookEntity : orderBookEntities) {
                final CopyBookEntity copyBookEntity = orderBookEntity.getCopyBookEntity();
                final BookEntity bookEntity = copyBookEntity.getBookEntity();

                stringBuilder.append("• ").append(bookEntity.getName()).append(" (").append(bookEntity.getAuthor())
                        .append(") (").append(orderBookEntity.getStatus()).append(")\n");
            }

            try {
                absSender.execute(
                        new AnswerCallbackQuery().setCallbackQueryId(callbackQuery.getId())
                );
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            return new EditMessageText()
                    .setChatId(callbackQuery.getMessage().getChatId())
                    .setMessageId(callbackQuery.getMessage().getMessageId())
                    .setText(stringBuilder.toString())
                    .setReplyMarkup(getInlineKeyboardMarkupBack());
        }

        return new AnswerCallbackQuery()
                .setCallbackQueryId(callbackQuery.getId())
                .setText("Данная команда доступна только авторизованным пользователям.")
                .setShowAlert(true);
    }

    private @NotNull BotApiMethod getSendMessageFormular(final AbsSender absSender, final CallbackQuery callbackQuery) {
        final Optional<AuthorizationEntity> optionalAuthorizationEntity = AuthorizationService.getInstance().read(callbackQuery.getFrom().getId());

        if (optionalAuthorizationEntity.isPresent()) {
            try {
                absSender.execute(
                        new AnswerCallbackQuery().setCallbackQueryId(callbackQuery.getId())
                );
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            return new EditMessageText()
                    .setChatId(callbackQuery.getMessage().getChatId())
                    .setMessageId(callbackQuery.getMessage().getMessageId())
                    .setText("Выберите команду " + EmojiUtils.BACKHAND)
                    .setReplyMarkup(getInlineKeyboardMarkupFormular());
        }

        return new AnswerCallbackQuery()
                .setCallbackQueryId(callbackQuery.getId())
                .setText("Данная команда доступна только авторизованным пользователям.")
                .setShowAlert(true);
    }

    private @NotNull SendMessage getSendMessageFormular(final Message message) {
        final long tgId = message.getFrom().getId();
        final long chatId = message.getChatId();

        final Optional<AuthorizationEntity> optionalAuthorizationEntity = AuthorizationService.getInstance().read(tgId);

        if (optionalAuthorizationEntity.isPresent()) {
            return new SendMessage()
                    .setChatId(chatId)
                    .setText("Выберите команду " + EmojiUtils.BACKHAND)
                    .setReplyMarkup(getInlineKeyboardMarkupFormular());
        }

        return MessagesUtils.getSendMessageWrong(chatId, "Эта команда доступна только авторизованным пользователям.");
    }

    private @NotNull InlineKeyboardMarkup getInlineKeyboardMarkupFormular() {
        return new InlineKeyboardMarkupBuilder()
                .row()
                .button(NAME_BUTTON_MY_BOOKS, CALLBACK_MY_BOOKS)
                .button(NAME_BUTTON_MY_ORDERS, CALLBACK_MY_ORDERS)
                .endRow()
                .build();
    }

    private @NotNull InlineKeyboardMarkup getInlineKeyboardMarkupBack() {
        return new InlineKeyboardMarkupBuilder()
                .row()
                .button(NAME_BUTTON_BACK, CALLBACK_BACK)
                .endRow()
                .build();
    }

}