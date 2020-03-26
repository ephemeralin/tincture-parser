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
import java.util.concurrent.*;
import java.util.logging.Logger;

public class FeedsExtractorService {

    private static final int THREADS_NUMBER = 4;
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
        ExecutorService taskExecutor = Executors.newFixedThreadPool(THREADS_NUMBER);
        CompletionService<CallResult> taskCompletionService =
                new ExecutorCompletionService<>(taskExecutor);
        if (feedSourcesProps != null) {
            int submittedTasks = 0;
            for (Map.Entry<Object, Object> entry : feedSourcesProps.entrySet()) {
                FeedSource feedSource = FeedSource.valueOf(entry.getKey().toString());
                String rssFeedUrl = entry.getValue().toString();
                String feedInfo = feedInfoProps.getProperty(entry.getKey().toString());
                taskCompletionService.submit(new CallableTask(feedSource, feedInfo, rssFeedUrl));
                submittedTasks++;
            }
            for (int tasksHandled = 0; tasksHandled < submittedTasks; tasksHandled++) {
                try {
                    log.info("trying to take from Completion service");
                    Future<CallResult> result = taskCompletionService.take();
                    log.info("result for a task available in queue.Trying to get()");
                    CallResult callResult = result.get();
                    if (!callResult.rssEntries.isEmpty()) {
                        RssFeed rssFeed = createRssFeed(callResult.feedInfo, callResult.feedSource);
                        rssFeed.setEntries(callResult.rssEntries);
                        rssFeedDAO.saveIfAbsent(rssFeed);
                    } else {
                        log.info("parsed rssEntries list is empty. Don't need to save");
                    }
                } catch (InterruptedException e) {
                    log.warning("Error Interrupted exception");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    log.warning("Error get() threw exception");
                    e.printStackTrace();
                }
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

    class CallableTask implements Callable<CallResult> {
        FeedSource feedSource;
        String feedInfo;
        String rssFeedUrl;

        CallableTask(FeedSource feedSource, String feedInfo, String rssFeedUrl) {
            this.feedSource = feedSource;
            this.feedInfo = feedInfo;
            this.rssFeedUrl = rssFeedUrl;
        }

        public CallResult call() throws Exception {
            String taskName = feedSource.name();
            log.info(" Task " + taskName + " started -----");
            RssParser parser = RssParserSimpleFactory.getParser(feedSource);
            List<RssEntry> rssEntries = parser.parse(rssFeedUrl);
            log.info(" Task " + taskName + " completed @@@@@@");
            return new CallResult(feedInfo, feedSource, rssEntries);
        }

    }

    class CallResult {
        String feedInfo;
        FeedSource feedSource;
        List<RssEntry> rssEntries;

        public CallResult(String feedInfo, FeedSource feedSource, List<RssEntry> rssEntries) {
            this.feedInfo = feedInfo;
            this.feedSource = feedSource;
            this.rssEntries = rssEntries;
        }
    }

}
