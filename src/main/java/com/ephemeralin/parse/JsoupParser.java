package com.ephemeralin.parse;

import com.ephemeralin.data.RssEntry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.ephemeralin.parse.Constants.MAX_FEED_SIZE;

public class JsoupParser implements RssParser {

    private final Logger log = Logger.getLogger(String.valueOf(this.getClass()));

    @Override
    public List<RssEntry> parse(String url) {
        List<RssEntry> rssEntries = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).execute().parse();
            final Elements itemElements = doc.getElementsByTag("item");
            itemElements.stream().limit(MAX_FEED_SIZE).forEach(item -> {
                RssEntry entry = new RssEntry(
                        item.getElementsByTag("title").text(),
                        item.getElementsByTag("description").text(),
                        item.getElementsByTag("link").text()
                );
                rssEntries.add(entry);
            });
        } catch (IOException ex) {
            ex.printStackTrace();
            log.warning("ERROR while parsing url: " + url);
            log.warning(ex.getMessage());
        }
        return rssEntries;
    }
}
