package com.ephemeralin.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.ephemeralin.service.FeedsExtractorService;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

public class AllFeedsExtractor implements RequestStreamHandler {

    private final Logger log = Logger.getLogger(String.valueOf(this.getClass()));

    private FeedsExtractorService feedsExtractorService;

    public AllFeedsExtractor() {
        this.feedsExtractorService = new FeedsExtractorService();
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) {
        log.info("--- start getting rss feeds");
        feedsExtractorService.downloadRssFeeds();
        log.info("--- stop getting rss feeds");
    }
}
