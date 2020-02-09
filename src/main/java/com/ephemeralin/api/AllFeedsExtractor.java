package com.ephemeralin.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.ephemeralin.crawl.RssParser;
import com.ephemeralin.crawl.RssParserSimpleFactory;
import com.ephemeralin.dao.RssFeedDAO;
import com.ephemeralin.data.FeedArea;
import com.ephemeralin.data.FeedSource;
import com.ephemeralin.data.RssEntry;
import com.ephemeralin.data.RssFeed;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class AllFeedsExtractor implements RequestStreamHandler {

    private final Logger log = Logger.getLogger(String.valueOf(this.getClass()));

    private RssFeedDAO rssFeedDAO;
    private Properties feedSourcesProps;
    private Properties feedInfoProps;

    public AllFeedsExtractor() {
        this.rssFeedDAO = RssFeedDAO.getInstance();
        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream("feedsources.properties")) {
            if (input == null) {
                log.warning("Unable to find feedsources.properties");
            }
            feedSourcesProps = new Properties();
            feedSourcesProps.load(input);
        } catch (IOException ex) {
            log.warning("Could not load feedsources.properties");
        }
        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream("feedinfo.properties")) {
            if (input == null) {
                log.warning("Unable to find feedinfo.properties");
            }
            feedInfoProps = new Properties();
            feedInfoProps.load(input);
        } catch (IOException ex) {
            log.warning("Could not load feedinfo.properties");
        }
    }

    private void downloadRssFeeds() {
        if (feedSourcesProps != null) {
            feedSourcesProps.forEach((key, value) -> {
                log.info("process " + key + " = " + value);
                FeedSource feedSource = FeedSource.valueOf(key.toString());
                String rssFeedUrl = value.toString();
                String feedInfo = feedInfoProps.getProperty(key.toString());

                FeedArea feedArea = null;
                String feedPrettyName = "";
                String feedHostUrl = "";
                int feedOrder = 0;
                if (feedInfo != null) {
                    String[] values = feedInfo.split(",");
                    feedArea = FeedArea.valueOf(values[0]);
                    feedOrder = Integer.parseInt(values[1]);
                    feedPrettyName = values[2];
                    feedHostUrl = values[3];
                }
                RssFeed rssFeed = new RssFeed();
                rssFeed.setFeedName(feedSource.name());
                rssFeed.setFeedArea(feedArea);
                rssFeed.setFeedPrettyName(feedPrettyName);
                rssFeed.setFeedHostUrl(feedHostUrl);
                rssFeed.setFeedOrder(feedOrder);

                ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
                rssFeed.setFeedUpdated(now.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));

                RssParser parser = RssParserSimpleFactory.getParser(feedSource);
                List<RssEntry> rssEntries = parser.parse(rssFeedUrl);
                rssFeed.setEntries(rssEntries);
                rssFeedDAO.save(rssFeed);
            });
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        log.info("--- start getting rss feeds");
        downloadRssFeeds();
        log.info("--- stop getting rss feeds");
    }
}
