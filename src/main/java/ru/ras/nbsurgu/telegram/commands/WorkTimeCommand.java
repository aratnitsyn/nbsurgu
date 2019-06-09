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
import ru.ras.nbsurgu.telegram.database.entity.DepartmentEntity;
import ru.ras.nbsurgu.telegram.database.entity.EmployeeEntity;
import ru.ras.nbsurgu.telegram.database.entity.StateEntity;
import ru.ras.nbsurgu.telegram.database.entity.WorkTimeEntity;
import ru.ras.nbsurgu.telegram.database.service.DepartmentService;
import ru.ras.nbsurgu.telegram.keyboard.InlineKeyboardMarkupBuilder;
import ru.ras.nbsurgu.telegram.utils.EmojiUtils;
import ru.ras.nbsurgu.telegram.utils.MessagesUtils;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class WorkTimeCommand implements ICommand {

    private static final String MESSAGE_WORK_TIME = EmojiUtils.TIME + " Режим работы";

    private static final String CALLBACK_DEPARTMENT_PREFIX = "callback_department_";
    private static final String CALLBACK_DEPARTMENT_BACK = CALLBACK_DEPARTMENT_PREFIX + "back";

    private static final String NAME_BUTTON_BACK = EmojiUtils.BACK.toString();

    private static final Set<String> callbacks = new HashSet<>();
    private static final Set<String> commands = new HashSet<>();
    private static final Set<Integer> states = new HashSet<>();

    public WorkTimeCommand() {
        commands.add(MESSAGE_WORK_TIME);

        final List<DepartmentEntity> departmentEntities = DepartmentService.getInstance().readAll();

        if (!departmentEntities.isEmpty()) {
            departmentEntities.forEach(
                    entity -> callbacks.add(CALLBACK_DEPARTMENT_PREFIX + entity.getId())
            );
        }

        callbacks.add(CALLBACK_DEPARTMENT_BACK);
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

        if (data.equals(CALLBACK_DEPARTMENT_BACK)) {
            return getSendMessageWorkTime(absSender, callbackQuery);
        } else if (data.startsWith(CALLBACK_DEPARTMENT_PREFIX)) {
            return getSendMessageDepartmentWorkTime(absSender, callbackQuery);
        }

        return MessagesUtils.getSendMessageWrong(callbackQuery.getMessage().getChatId(), "Не обработанная команда.");
    }

    @Override
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final StateEntity stateEntity) {
        return MessagesUtils.getSendMessageWrong(update.getMessage().getChatId(), "Не обработанная команда.");
    }

    @Override
    public @NotNull BotApiMethod execute(final AbsSender absSender, final Update update, final Message message) {
        if (message.getText().equals(MESSAGE_WORK_TIME)) {
            return getSendMessageWorkTime(message);
        }

        return MessagesUtils.getSendMessageWrong(update.getMessage().getChatId(), "Не обработанная команда.");
    }

    private @NotNull SendMessage getSendMessageWorkTime(final Message message) {
        final List<DepartmentEntity> departmentEntities = DepartmentService.getInstance().readAll();

        if (departmentEntities.isEmpty()) {
            return MessagesUtils.getSendMessageWrong(message.getChatId(), "В данный момент список отделов пуст.");
        }

        return new SendMessage()
                .setChatId(message.getChatId())
                .setText("Выберите отдел " + EmojiUtils.BACKHAND)
                .setReplyMarkup(getInlineKeyboardMarkupDepartments(departmentEntities));
    }

    private EditMessageText getSendMessageWorkTime(final AbsSender absSender, final CallbackQuery callbackQuery) {
        final List<DepartmentEntity> departmentEntities = DepartmentService.getInstance().readAll();

        try {
            absSender.execute(new AnswerCallbackQuery().setCallbackQueryId(callbackQuery.getId()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        if (departmentEntities.isEmpty()) {
            return new EditMessageText()
                    .setChatId(callbackQuery.getMessage().getChatId())
                    .setMessageId(callbackQuery.getMessage().getMessageId())
                    .setText(EmojiUtils.WARNING + " В данный момент список отделов пуст.");
        }

        return new EditMessageText()
                .setChatId(callbackQuery.getMessage().getChatId())
                .setMessageId(callbackQuery.getMessage().getMessageId())
                .setText("Выберите отдел " + EmojiUtils.BACKHAND)
                .setReplyMarkup( getInlineKeyboardMarkupDepartments(departmentEntities));
    }

    private BotApiMethod getSendMessageDepartmentWorkTime(final AbsSender absSender, final CallbackQuery callbackQuery) {
        final String data = callbackQuery.getData();

        final long departmentId = Long.parseLong(data.substring(CALLBACK_DEPARTMENT_PREFIX.length(), data.length()));

        final StringBuilder stringBuilder = new StringBuilder();

        final DepartmentEntity departmentEntity = DepartmentService.getInstance().read(departmentId);

        Set<WorkTimeEntity> workTimeEntities = DepartmentService.getInstance().loadWorkTime(departmentId);

        if (!workTimeEntities.isEmpty()) {
            workTimeEntities = workTimeEntities.stream().sorted(Comparator.comparing(WorkTimeEntity::getDay))
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            stringBuilder
                    .append(EmojiUtils.CALENDAR).append(" Режим работы отдела")
                    .append(" \"").append(departmentEntity.getName()).append("\"\n");

            for (WorkTimeEntity entity : workTimeEntities) {
                if (entity.getDay() == 0) {
                    continue;
                }

                stringBuilder
                        .append(DayOfWeek.of(entity.getDay())
                                .getDisplayName(TextStyle.SHORT, new Locale("ru")))
                        .append(":").append(EmojiUtils.TAB).append(entity.getStartWork()).append(" - ")
                        .append(entity.getEndWork());

                if (entity.getStartLunch() != null) {
                    stringBuilder
                            .append(" (Обед: ")
                            .append(entity.getStartLunch()).append(" - ").append(entity.getEndLunch())
                            .append(" )");
                }

                stringBuilder.append("\n");
            }

            final Optional<WorkTimeEntity> workTimeEntityNotice =
                    workTimeEntities.stream().filter(entity -> entity.getDay() == 0).findFirst();

            workTimeEntityNotice.ifPresent(workTimeEntity ->
                    stringBuilder.append("Примечание: ").append(workTimeEntity.getNotice()));
        }

        final Set<EmployeeEntity> employeeEntities = DepartmentService.getInstance().loadEmployee(departmentId);

        if (!employeeEntities.isEmpty()) {
            stringBuilder.append("Контактные лица:\n");

            for (EmployeeEntity entity : employeeEntities) {
                stringBuilder
                        .append(EmojiUtils.SILHOUETTE_MAN).append(" ")
                        .append(entity.getSurname()).append(" ")
                        .append(entity.getName()).append(" ")
                        .append(entity.getMiddleName());

                final String phoneNumber = entity.getPhoneNumber();

                if (!phoneNumber.isEmpty()) {
                    stringBuilder.append(" ").append(EmojiUtils.TELEPHONE).append(" ").append(entity.getPhoneNumber());
                }

                final String cabinet = entity.getCabinet();

                if (!cabinet.isEmpty()) {
                    stringBuilder.append(" ").append(EmojiUtils.DOOR).append(" ").append(entity.getCabinet());
                }
            }
        }

        if (stringBuilder.length() == 0) {
            stringBuilder.append(EmojiUtils.WRONG).append(" Данных по этому отделу нет.");
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

    private InlineKeyboardMarkup getInlineKeyboardMarkupDepartments(final List<DepartmentEntity> departmentEntities) {
        final InlineKeyboardMarkupBuilder inlineKeyboardMarkupBuilder = new InlineKeyboardMarkupBuilder();

        inlineKeyboardMarkupBuilder.row();

        int counterButton = 0;

        for (DepartmentEntity entity : departmentEntities) {
            counterButton++;

            inlineKeyboardMarkupBuilder.button(
                    entity.getName(),
                    CALLBACK_DEPARTMENT_PREFIX + entity.getId()
            );

            if (counterButton == 2) {
                counterButton = 0;

                inlineKeyboardMarkupBuilder.endRow();
                inlineKeyboardMarkupBuilder.row();
            }
        }

        inlineKeyboardMarkupBuilder.endRow();

        return inlineKeyboardMarkupBuilder.build();
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkupBack() {
        return new InlineKeyboardMarkupBuilder()
                .row()
                .button(NAME_BUTTON_BACK, CALLBACK_DEPARTMENT_BACK)
                .endRow()
                .build();
    }

}