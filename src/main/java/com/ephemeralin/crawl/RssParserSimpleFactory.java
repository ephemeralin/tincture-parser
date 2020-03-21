package com.ephemeralin.crawl;

import com.ephemeralin.data.FeedSource;

public class RssParserSimpleFactory {

    public static RssParser getParser(FeedSource source) {
        RssParser parser = null;
        switch (source) {
            case devby:
                parser = new DevbyParser();
                break;

            case reddit_tech:
            case reddit_prog:
            case reddit_java:
            case spring:
                parser = new RedditParser();
                break;

            case vc:
            case devcom:
            case rbc:
            case wash_post:
                parser = new JsoupParser();
                break;

            default:
                parser = new GenericParser();
                break;
        }
        return parser;
    }

}
