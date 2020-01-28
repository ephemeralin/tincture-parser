package com.ephemeralin.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.ephemeralin.dao.RssFeedDAO;
import com.ephemeralin.data.FeedSource;
import com.ephemeralin.data.RssEntry;
import com.ephemeralin.data.RssFeed;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AllFeedsExtractor implements RequestStreamHandler {

    private final Logger log = Logger.getLogger(String.valueOf(this.getClass()));

    private RssFeedDAO rssFeedDAO;

    public AllFeedsExtractor() {
        this.rssFeedDAO = RssFeedDAO.getInstance();
    }

    private void downloadRssFeeds() {
        boolean ok = false;
        try {
            URL feedUrl = new URL("https://habr.com/ru/rss/best/daily/");
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            List<RssEntry> rssEntries = feed.getEntries().stream().map(e ->
                    new RssEntry(e.getTitle(), e.getDescription().getValue(), e.getLink(), FeedSource.HABR))
                    .collect(Collectors.toList());
            RssFeed rssFeed = new RssFeed();
            rssFeed.setFeedName(FeedSource.HABR.name());
            rssFeed.setEntries(rssEntries);
            rssFeedDAO.save(rssFeed);
            ok = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.warning("ERROR: " + ex.getMessage());
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        log.info("--- start getting rss feeds");
        downloadRssFeeds();
        log.info("--- stop getting rss feeds");
    }
}
