package com.ras.nbsurgu.telegram.utils;

import com.ras.nbsurgu.telegram.events.CommandEvents;
import com.ras.nbsurgu.telegram.events.CallbackEvents;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Класс <code>XMLParser</code> формирует список событий, которые
 * бот должен обработать. События описаны в XML файле <code>events.xml</code>.
 * При анализировании XML файла, события запаковываются в соответствующие
 * классы для дальнейшей выборки и обработки.
 * События представлены двумя видами:
 *      1. Команда (текстовое сообщение).
 *      2. Обратный вызов (callback).
 * Список формируется единовременно, при запуске бота.
 */
public class XMLParser {

    public void init() throws ParserConfigurationException, IOException, SAXException {
        final CommandEvents commandEvents = CommandEvents.getInstance();
        final CallbackEvents callbackEvents = CallbackEvents.getInstance();

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document document = builder.parse(getClass().getClassLoader().getResourceAsStream("events.xml"));

        final Node nodeEvents = document.getFirstChild();
        final Element elementEvents = (Element) nodeEvents;

        final NodeList nodeListEvents = elementEvents.getElementsByTagName("class");
        for (int _class = 0; _class < nodeListEvents.getLength(); _class++) {
            Element elementClass = (Element) nodeListEvents.item(_class);

            List<String> commands = new ArrayList<>();
            List<String> callbacks = new ArrayList<>();

            final NodeList nodeListClass = elementClass.getElementsByTagName("add");
            for (int _add = 0; _add < nodeListClass.getLength(); _add++) {
                Element elementAdd = (Element) nodeListClass.item(_add);

                String type = elementAdd.getAttribute("type");

                if (type.equals("command")) {
                    commands.add(type);
                } else if (type.equals("callback")) {
                    callbacks.add(type);
                }
            }

            String className = elementClass.getAttribute("name");

            if (!commands.isEmpty()) {
                commandEvents.add(
                        className,
                        commands
                );
            }

            if (!callbacks.isEmpty()) {
                callbackEvents.add(
                        className,
                        callbacks
                );
            }
        }
    }

}