package com.ephemeralin.service_old;

import com.ephemeralin.data.FeedArea;
import com.ephemeralin.data.RssFeed;
import com.ephemeralin.service.RssFeedDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class RssFeedServiceImpl implements RssFeedService {

    private static RssFeedDAO rssFeedDAO = RssFeedDAO.getInstance();
    private final Logger log = LogManager.getLogger(this.getClass());

    @Override
    public List<RssFeed> listByFeedArea(FeedArea feedArea) {
        return rssFeedDAO.queryByFeedArea(feedArea);
    }
}
