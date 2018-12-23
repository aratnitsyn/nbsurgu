package utils;

import com.ras.nbsurgu.telegram.utils.Emoji;
import com.ras.nbsurgu.telegram.utils.XMLParser;
import com.ras.nbsurgu.telegram.events.CommandEvents;

import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;
import org.junit.Before;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;

public class XMLParserTest {

    private final String START_CLASS = "com.ras.nbsurgu.telegram.commands.Start";
    private final String HELP_CLASS = "com.ras.nbsurgu.telegram.commands.Help";
    private final String NEWS_CLASS = "com.ras.nbsurgu.telegram.commands.News";

    @Before
    public void before() {
        try {
            new XMLParser().init();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testContain() {
        final CommandEvents commandEvents = CommandEvents.getInstance();

        assert commandEvents.getClassName("/start").equals(START_CLASS);

        assert commandEvents.getClassName("/help").equals(HELP_CLASS);

        assert commandEvents.getClassName("/news").equals(NEWS_CLASS);
        assert commandEvents.getClassName(Emoji.NEWS + " Новости").equals(NEWS_CLASS);
    }

}