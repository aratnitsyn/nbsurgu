package ru.ras.nbsurgu.telegram.handlers;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import ru.ras.nbsurgu.telegram.commands.*;
import ru.ras.nbsurgu.telegram.database.entity.StateEntity;
import ru.ras.nbsurgu.telegram.database.service.StateService;
import ru.ras.nbsurgu.telegram.utils.EmojiUtils;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class WebhookHandlers extends Handlers {

    private static volatile WebhookHandlers instance;

    private static final Logger logger = LoggerFactory.getLogger(WebhookHandlers.class);

    private static final CommandHandlers commandHandlers = new CommandHandlers();

    private static Set<Map.Entry<Object, Object>> streamCommandHandler = null;

    private WebhookHandlers() {
        commandHandlers.registerCommand(new HelpCommand());
        commandHandlers.registerCommand(new NewsCommand());
        commandHandlers.registerCommand(new StartCommand());
        commandHandlers.registerCommand(new CancelCommand());
        commandHandlers.registerCommand(new ProfileCommand());
        commandHandlers.registerCommand(new WorkTimeCommand());
        commandHandlers.registerCommand(new FormularCommand());
        commandHandlers.registerCommand(new AuthorizationCommand());
        commandHandlers.registerCommand(new ForgetPasswordCommand());

        streamCommandHandler = commandHandlers.getEvents().entrySet();
    }

    public static WebhookHandlers getInstance() {
        if (instance == null) {
            synchronized (WebhookHandlers.class) {
                if (instance == null) {
                    instance = new WebhookHandlers();
                }
            }
        }

        return instance;
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(final Update update) {
        final Message message = update.getMessage();

        BotApiMethod sendingMessage = null;

        if (update.hasCallbackQuery()) {
            sendingMessage = getSendingMessage(this, update, update.getCallbackQuery());
        } else if (update.hasMessage()) {
            if (message.hasText()) {
                sendingMessage = getSendingMessage(this, update, message);
            }

            if (sendingMessage == null || message.hasContact()) {
                final Optional<StateEntity> optional =
                        StateService.getInstance().read(message.getFrom().getId());

                if (optional.isPresent()) {
                    sendingMessage = getSendingMessage(this, update, optional.get());
                }
            }
        }

        return sendingMessage == null ? getSendMessageUnknownCommand(update.getMessage().getChatId()) : sendingMessage;
    }

    private @Nullable BotApiMethod getSendingMessage(final AbsSender absSender, final Update update, final CallbackQuery callbackQuery) {
        final String data = callbackQuery.getData();

        if (commandHandlers.getEvents().containsKey(data)) {
            final Optional<Map.Entry<Object, Object>> optionalEntry = streamCommandHandler.stream()
                    .filter(entry -> entry.getKey().equals(data))
                    .findFirst();

            if (optionalEntry.isPresent()) {
                final ICommand handler = (ICommand) optionalEntry.get().getValue();

                return handler.execute(absSender, update, callbackQuery);
            }
        }

        return null;
    }

    private @Nullable BotApiMethod getSendingMessage(final AbsSender absSender, final Update update, final StateEntity stateEntity) {
        final int state = stateEntity.getState();

        if (commandHandlers.getEvents().containsKey(state)) {
            final Optional<Map.Entry<Object, Object>> optionalEntry = streamCommandHandler.stream()
                    .filter(entry -> entry.getKey().equals(state))
                    .findFirst();

            if (optionalEntry.isPresent()) {
                final ICommand handler = (ICommand) optionalEntry.get().getValue();

                return handler.execute(absSender, update, stateEntity);
            }
        }

        return null;
    }

    private @Nullable BotApiMethod getSendingMessage(final AbsSender absSender, final Update update, final Message message) {
        final String text = message.getText();

        if (commandHandlers.getEvents().containsKey(text)) {
            final Optional<Map.Entry<Object, Object>> optionalEntry = streamCommandHandler.stream()
                    .filter(entry -> entry.getKey().equals(text))
                    .findFirst();

            if (optionalEntry.isPresent()) {
                final ICommand handler = (ICommand) optionalEntry.get().getValue();

                return handler.execute(absSender, update, message);
            }
        }

        return null;
    }

    private SendMessage getSendMessageUnknownCommand(final long chatId) {
        return new SendMessage()
                .setChatId(chatId)
                .setText("Такой команды нет " + EmojiUtils.HMM + "\nВведите \"/help\", чтобы получить список команд.");
    }

}