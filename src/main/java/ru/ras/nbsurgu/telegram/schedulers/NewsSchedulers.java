package ru.ras.nbsurgu.telegram.schedulers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.ras.nbsurgu.telegram.utils.LibraryNewsUtils;

import java.io.IOException;
import java.util.Calendar;

import static java.lang.Math.abs;

public class NewsSchedulers implements ISchedulers {

    private static final Logger logger = LoggerFactory.getLogger(NewsSchedulers.class);

    private final long EVERY_HOUR = 3_600_000L;

    private final long INIT_TIME;
    private final long DELAY_TIME = EVERY_HOUR;

    private static final String SITE_URL = "http://www.lib.surgu.ru/";
    private static final String SITE_URL_NEWS = "http://www.lib.surgu.ru/index.php?view=s&sid=30";

    private static final LibraryNewsUtils libraryNews = LibraryNewsUtils.getInstance();

    public NewsSchedulers() {
        final Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        final long millis = calendar.getTimeInMillis() - System.currentTimeMillis();

        INIT_TIME = millis < 0 ? (EVERY_HOUR - abs(millis)) : millis;
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
        Document document = null;

        try {
            document = Jsoup.connect(SITE_URL_NEWS).get();
        } catch (IOException e) {
            logger.warn("[News Schedulers] Failed to established connection (" + e.getMessage() + ")");
        }

        assert document != null;

        final Elements tableElements = document.select("td[class=main-content]");

        if (tableElements.size() > 0) {
            libraryNews.clear();

            for (Element tableElement : tableElements) {
                final Elements rowElements = tableElement.getAllElements();

                final Element elementTitle = rowElements.select("p").first();
                final Element elementUrl = rowElements.select("a:contains(подробнее)").first();

                if (elementUrl == null) {
                    continue;
                }

                final String title = elementTitle.text().trim();
                final String url = SITE_URL + elementUrl.attr("href");

                libraryNews.add(title, url);
            }
        }
    }

}
