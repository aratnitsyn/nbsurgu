package com.ras.nbsurgu.telegram.handlers;

import com.ras.nbsurgu.telegram.settings.Config;

import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public class WebhookHandlers extends TelegramWebhookBot {

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.hasCallbackQuery()) {
                return new CallbackHandlers().execute(update);
            } else {
                if (update.getMessage().hasText()) {
                    return new CommandHandlers().execute(update);
                }
            }
        }

        return null;
    }

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