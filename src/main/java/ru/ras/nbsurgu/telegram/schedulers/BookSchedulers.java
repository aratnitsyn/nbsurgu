package ru.ras.nbsurgu.telegram.schedulers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ru.ras.nbsurgu.telegram.database.entity.*;
import ru.ras.nbsurgu.telegram.database.service.TakenBookService;
import ru.ras.nbsurgu.telegram.handlers.WebhookHandlers;
import ru.ras.nbsurgu.telegram.utils.EmojiUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

public class BookSchedulers implements ISchedulers {

    private final long HOURS_ONE_DAY = 86_400_000L;

    private final long INIT_TIME;
    private final long DELAY_TIME = HOURS_ONE_DAY;

    public BookSchedulers() {
        final Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        final long millis = calendar.getTimeInMillis() - System.currentTimeMillis();

        INIT_TIME = millis < 0 ? (HOURS_ONE_DAY - abs(millis)) : millis;
    }

    @Override
    public long getInitTime() {
        return INIT_TIME;
    }

    @Override
    public long getDelayTime() {
        return DELAY_TIME;
    }

    @Override
    public void run() {
        final List<TakenBookEntity> takenBookEntities = TakenBookService.getInstance().readOverdue();

        if (!takenBookEntities.isEmpty()) {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

            final Map<Long, String> userOverdueBooks = new HashMap<>();

            for (TakenBookEntity takenBookEntity : takenBookEntities) {
                final UserEntity userEntity = takenBookEntity.getUserId();
                final AuthorizationEntity authorizationEntity = userEntity.getAuthorization();

                if (authorizationEntity == null) {
                    continue;
                }

                final long tgId = authorizationEntity.getTgId();

                final StringBuilder stringBuilder = new StringBuilder();

                if (userOverdueBooks.isEmpty() || !userOverdueBooks.containsKey(tgId)) {
                    stringBuilder.append(EmojiUtils.EXCLAMATION_MARK).append("Напоминание").append(EmojiUtils.EXCLAMATION_MARK)
                            .append("\n").append(userEntity.getName()).append(", подходит срок сдачи книг:");
                } else {
                    stringBuilder.append(userOverdueBooks.get(tgId));
                }

                final CopyBookEntity copyBookEntity = takenBookEntity.getCopyBookEntity();
                final BookEntity bookEntity = copyBookEntity.getBookEntity();

                stringBuilder.append("\n").append("• ").append(bookEntity.getName()).append(" (")
                        .append(dateFormat.format(takenBookEntity.getDateReturn())).append(")");

                userOverdueBooks.put(tgId, stringBuilder.toString());
            }

            final AbsSender absSender = WebhookHandlers.getInstance();

            for (Map.Entry<Long, String> entry : userOverdueBooks.entrySet()) {
                try {
                    absSender.execute(new SendMessage().setChatId(entry.getKey()).setText(entry.getValue()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}