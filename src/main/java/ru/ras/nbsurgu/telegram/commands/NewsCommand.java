package ru.ras.nbsurgu.telegram.commands;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.ras.nbsurgu.telegram.database.entity.StateEntity;
import ru.ras.nbsurgu.telegram.keyboard.InlineKeyboardMarkupBuilder;
import ru.ras.nbsurgu.telegram.utils.EmojiUtils;
import ru.ras.nbsurgu.telegram.utils.LibraryNewsUtils;
import ru.ras.nbsurgu.telegram.utils.MessagesUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewsCommand implements ICommand{

    private static final String MESSAGE_NEWS = EmojiUtils.NEWS + " Новости";

    private static final Set<String> callbacks = new HashSet<>();
    private static final Set<String> commands = new HashSet<>();
    private static final Set<Integer> states = new HashSet<>();

    public NewsCommand() {
        commands.add(MESSAGE_NEWS);
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
        if (message.getText().equals(MESSAGE_NEWS)) {
            return getSendMessageNews(message);
        }

        return MessagesUtils.getSendMessageWrong(message.getChatId(), "Не обработанная команда.");
    }

    private @NotNull SendMessage getSendMessageNews(final Message message) {
        final List<Pair<String, String>> news = LibraryNewsUtils.getInstance().get();

        final StringBuilder stringBuilder = new StringBuilder();

        if (news.size() == 0) {
            stringBuilder.append("В данный момент новостей нет.");
        } else {
            final InlineKeyboardMarkupBuilder inlineKeyboardMarkupBuilder = new InlineKeyboardMarkupBuilder();

            for (Pair<String, String> pair : news) {
                stringBuilder.append(pair.getKey()).append("\n");

                inlineKeyboardMarkupBuilder.row();

                try {
                    inlineKeyboardMarkupBuilder.button(pair.getKey().substring(0, 10), new URL(pair.getValue()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                inlineKeyboardMarkupBuilder.endRow();
            }

            return new SendMessage()
                    .setChatId(message.getChatId())
                    .setText(stringBuilder.toString())
                    .setReplyMarkup(inlineKeyboardMarkupBuilder.build());
        }

        return new SendMessage()
                .setChatId(message.getChatId())
                .setText(stringBuilder.toString());
    }

}