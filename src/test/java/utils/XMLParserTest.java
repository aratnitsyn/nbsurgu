package utils;

import com.ras.nbsurgu.telegram.events.CommandEvents;
import com.ras.nbsurgu.telegram.events.CallbackEvents;

import org.junit.Test;
import org.junit.Before;

import java.util.List;
import java.util.ArrayList;

public class XMLParserTest {

    private final String FIRST_CLASS = "class_first";
    private final String SECOND_CLASS = "class_second";
    private final String THIRD_CLASS = "class_third";

    private final CommandEvents commandEvents = CommandEvents.getInstance();
    private final CallbackEvents callbackEvents = CallbackEvents.getInstance();

    @Before
    public void before() {
        List<String> listCommands__First = new ArrayList<>();
        listCommands__First.add("Войти");

        List<String> listCommands__Second = new ArrayList<>();
        listCommands__Second.add("Авторизация");
        listCommands__Second.add("/start");

        List<String> listCommands__Third = new ArrayList<>();
        listCommands__Third.add("/help");

        List<String> listCallbacks__First = new ArrayList<>();
        listCallbacks__First.add("callback_join");
        listCallbacks__First.add("callback_button_1");

        List<String> listCallbacks__Second = new ArrayList<>();
        listCallbacks__Second.add("callback_settings");
        listCallbacks__Second.add("callback_pay_state");

        commandEvents.add(FIRST_CLASS, listCommands__First);
        commandEvents.add(SECOND_CLASS, listCommands__Second);
        commandEvents.add(THIRD_CLASS, listCommands__Third);

        callbackEvents.add(FIRST_CLASS, listCallbacks__First);
        callbackEvents.add(SECOND_CLASS, listCallbacks__Second);
    }

    @Test
    public void testContain() {
        assert commandEvents.getClassName("Войти").equals(FIRST_CLASS);
        assert commandEvents.getClassName("Авторизация").equals(SECOND_CLASS);
        assert commandEvents.getClassName("/start").equals(SECOND_CLASS);
        assert commandEvents.getClassName("/help").equals(THIRD_CLASS);

        assert callbackEvents.getClassName("callback_join").equals(FIRST_CLASS);
        assert callbackEvents.getClassName("callback_button_1").equals(FIRST_CLASS);
        assert callbackEvents.getClassName("callback_settings").equals(SECOND_CLASS);
        assert callbackEvents.getClassName("callback_pay_state").equals(SECOND_CLASS);

        assert commandEvents.getClassName("Команда 1") == null;
        assert callbackEvents.getClassName("Обратный вызов 1") == null;
    }

}