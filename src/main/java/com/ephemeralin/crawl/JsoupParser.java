package com.ephemeralin.crawl;

import com.ephemeralin.data.RssEntry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class JsoupParser implements RssParser {

    private final Logger log = Logger.getLogger(String.valueOf(this.getClass()));

    @Override
    public List<RssEntry> parse(String url) {
        List<RssEntry> rssEntries = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).execute().parse();
            final Elements itemElements = doc.getElementsByTag("item");
            for (Element itemElement : itemElements) {
                RssEntry entry = new RssEntry(
                        itemElement.getElementsByTag("title").text(),
                        itemElement.getElementsByTag("description").text(),
                        itemElement.getElementsByTag("link").text()
                );
                rssEntries.add(entry);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            log.warning("ERROR while parsing url: " + url);
            log.warning(ex.getMessage());
        }
        return rssEntries;
    }
}
