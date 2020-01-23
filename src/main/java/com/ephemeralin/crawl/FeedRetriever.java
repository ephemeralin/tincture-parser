package com.ephemeralin.crawl;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

public class FeedRetriever implements RequestStreamHandler {

    private final Logger log = Logger.getLogger(String.valueOf(this.getClass()));

    public FeedRetriever() {
    }

    private void getRssFeeds() {
        log.info("--- start getting rss feeds");
        log.info("--- stop getting rss feeds");
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        getRssFeeds();
    }
}
