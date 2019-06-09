package ru.ras.nbsurgu.telegram.handlers;

import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import ru.ras.nbsurgu.telegram.settings.Config;

public abstract class Handlers extends TelegramWebhookBot {

    @Override
    public abstract BotApiMethod onWebhookUpdateReceived(final Update update);

    @Override
    public String getBotUsername() {
        return Config.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return Config.BOT_TOKEN;
    }

    @Override
    public String getBotPath() {
        return Config.WEBHOOK_NAME;
    }

}