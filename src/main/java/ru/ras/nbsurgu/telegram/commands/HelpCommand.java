package ru.ras.nbsurgu.telegram.commands;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import ru.ras.nbsurgu.telegram.database.entity.StateEntity;
import ru.ras.nbsurgu.telegram.utils.EmojiUtils;
import ru.ras.nbsurgu.telegram.utils.MessagesUtils;

import java.util.HashSet;
import java.util.Set;

public class HelpCommand implements ICommand {

    private static final String COMMAND_HELP = "/help";

    private static final Set<String> callbacks = new HashSet<>();
    private static final Set<String> commands = new HashSet<>();
    private static final Set<Integer> states = new HashSet<>();

    public HelpCommand() {
        commands.add(COMMAND_HELP);
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
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final StateEntity message) {
        return MessagesUtils.getSendMessageWrong(update.getMessage().getChatId(), "Не обработанная команда.");
    }

    @Override
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final Message message) {
        if (message.getText().equals(COMMAND_HELP)) {
            return getSendMessageStart(message);
        }

        return MessagesUtils.getSendMessageWrong(message.getChatId(), "Не обработанная команда.");
    }

    private @NotNull SendMessage getSendMessageStart(final Message message) {
        return new SendMessage()
                .setChatId(message.getChatId())
                .setText(EmojiUtils.EXCLAMATION_MARK +
                        "Вы можете выбирать команды из меню, либо присылать текстовые.\n\n" +
                        "Смотрите какие текстовые есть:\n" +
                        EmojiUtils.BUTTON_ONE + " \"/start\" - начать общение с ботом.\n" +
                        EmojiUtils.BUTTON_TWO + " \"/cancel\" - отмена текущей команды.\n"
                );
    }

}