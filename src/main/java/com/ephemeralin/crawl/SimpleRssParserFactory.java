package com.ephemeralin.crawl;

import com.ephemeralin.data.FeedSource;

public class SimpleRssParserFactory {

    public static RssParser getParser(FeedSource source) {
        return new GenericParser();
    }

}
