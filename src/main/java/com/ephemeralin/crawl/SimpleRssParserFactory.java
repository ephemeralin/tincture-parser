package com.ephemeralin.crawl;

import com.ephemeralin.data.FeedSource;

public class SimpleRssParserFactory {

    public static RssParser getParser(FeedSource source) {
        RssParser parser = null;
        switch (source) {
            case devby:
                parser = new DevbyParser();
                break;
            default:
                parser = new GenericParser();
                break;
        }
        return parser;
    }

}
