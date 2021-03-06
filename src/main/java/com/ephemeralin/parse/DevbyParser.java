package com.ephemeralin.parse;

import com.ephemeralin.data.RssEntry;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.ephemeralin.parse.Constants.MAX_FEED_SIZE;

public class DevbyParser implements RssParser {

    private final Logger log = Logger.getLogger(String.valueOf(this.getClass()));

    @Override
    public List<RssEntry> parse(String url) {
        List<RssEntry> rssEntries = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", "", Parser.xmlParser());
            final Elements itemElements = doc.getElementsByTag("item");
            itemElements.stream().limit(MAX_FEED_SIZE).forEach(item -> {
                RssEntry entry = new RssEntry(
                        removeCDATAtag(item.getElementsByTag("title").text()),
                        removeCDATAtag(item.getElementsByTag("description").text()),
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

    private String removeCDATAtag(String s) {
        String s1 = StringUtils.removeStart(s, "<![CDATA[");
        s1 = StringUtils.removeEnd(s1, "]]>");
        return StringUtils.trim(s1);
    }
}
