package com.ephemeralin.crawl;

import com.ephemeralin.data.RssEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GenericParser implements RssParser {

    private final Logger log = Logger.getLogger(String.valueOf(this.getClass()));

    public static String removeCDATA(String text) {
        StringBuilder resultString = new StringBuilder();
        Pattern regex = Pattern.compile("(?<!(<!\\[CDATA\\[))|((.*)\\w+\\W)");
        Matcher regexMatcher = regex.matcher(text);
        while (regexMatcher.find()) {
            resultString.append(regexMatcher.group());
        }
        return resultString.toString();
    }

    @Override
    public List<RssEntry> parse(String url) {
        List<RssEntry> rssEntries = Collections.EMPTY_LIST;
        try {
            URL feedUrl = new URL(url);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            rssEntries = feed.getEntries().stream().limit(10).map(e ->
                    new RssEntry(
                            e.getTitle(),
                            e.getDescription().getValue(),
                            e.getLink()
                    )
            ).collect(Collectors.toList());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.warning("ERROR while parsing url: " + url);
            log.warning(ex.getMessage());
        }
        return rssEntries;
    }
}
