package com.ephemeralin.service_old;

import com.ephemeralin.data.FeedArea;
import com.ephemeralin.data.RssFeed;

import java.util.List;

public interface RssFeedService {

    List<RssFeed> listByFeedArea(FeedArea feedArea);
}
