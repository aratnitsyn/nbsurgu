package com.ras.nbsurgu;

import com.ras.nbsurgu.telegram.Telegram;
import com.ras.nbsurgu.telegram.utils.XMLParser;

import java.io.IOException;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class Main {

    public static void main(String[] args) {
        init();

        new Telegram().start();
    }

    private static void init() {
        try {
            new XMLParser().init();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

}