package com.ephemeralin.service;

import com.ephemeralin.dao.RssFeedDAO;
import com.ephemeralin.data.FeedArea;
import com.ephemeralin.data.FeedSource;
import com.ephemeralin.data.RssEntry;
import com.ephemeralin.data.RssFeed;
import com.ephemeralin.parse.RssParser;
import com.ephemeralin.parse.RssParserSimpleFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class FeedsExtractorService {

    private final Logger log = Logger.getLogger(String.valueOf(this.getClass()));

    private RssFeedDAO rssFeedDAO;
    private Properties feedSourcesProps;
    private Properties feedInfoProps;

    public FeedsExtractorService() {
        log.info("--- start creating FeedsExtractor");
        this.rssFeedDAO = new RssFeedDAO();
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
        log.info("--- finish creating FeedsExtractor");
    }

    public void downloadRssFeeds() {
        if (feedSourcesProps != null) {
            for (Map.Entry<Object, Object> entry : feedSourcesProps.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                log.info("process " + key + " = " + value);
                FeedSource feedSource = FeedSource.valueOf(key.toString());
                String rssFeedUrl = value.toString();
                String feedInfo = feedInfoProps.getProperty(key.toString());

                RssParser parser = RssParserSimpleFactory.getParser(feedSource);
                List<RssEntry> rssEntries = parser.parse(rssFeedUrl);
                if (rssEntries.isEmpty()) {
                    log.info("parsed rssEntries list is empty. Don't need to save");
                    continue;
                }
                RssFeed rssFeed = createRssFeed(feedInfo, feedSource);
                rssFeed.setEntries(rssEntries);
                rssFeed.setFeedHash(rssFeed.hashCode());
                rssFeedDAO.saveIfAbsent(rssFeed);
            }
        }
    }

    private RssFeed createRssFeed(String feedInfo, FeedSource feedSource) {
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
        return rssFeed;
    }
}
