package com.ephemeralin.service;

import com.ephemeralin.data.FeedArea;
import com.ephemeralin.data.RssFeed;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RssFeedCache {

    public static final int TIN_CACHE_LIFETIME = 60 * 60 * 24 * 20;
    public static final String CACHE_ADDRESS = System.getenv("CACHE_ADDRESS");
    public static final List<String> cacheNodes = new ArrayList<>(Arrays.asList(System.getenv("CACHE_NODE_ADDRESS")));
    private static final Logger log = Logger.getLogger(String.valueOf(RssFeedCache.class));

    public RssFeedCache() {
    }

    public List<RssFeed> getByFeedArea(FeedArea feedArea) {
        log.info("start querying by feed area " + feedArea);
        List<RssFeed> rssFeeds = null;
        try {
            log.info("start creating MemcachedClient");
            List<InetSocketAddress> addresses = AddrUtil.getAddresses(cacheNodes);
            log.info("memcached addresses: " + addresses);
            MemcachedClient cache = new MemcachedClient(new InetSocketAddress(CACHE_ADDRESS, 11211));
//            MemcachedClient cache = new MemcachedClient(AddrUtil.getAddresses(cacheNodes));
            log.info("finish creating MemcachedClient");
            log.info("start loading from cache");
            Object result = cache.get(feedArea.name());
            log.info("result from cache: " + result);
            rssFeeds = (List<RssFeed>) result;
            log.info("loaded from cache");
        } catch (Exception e) {
            log.warning("Cannot use MemcachedClient while querying :(");
            log.warning("Exception msg: " + e.getLocalizedMessage());
            log.warning("start slow retrieving from DB");
            log.warning("finish slow retrieving from DB");
        }
        return rssFeeds;
    }

    public void setByFeedArea(String feedAreaName, List<RssFeed> rssFeedList) {
        try {
            log.info("start saving to cache");
            MemcachedClient cache = new MemcachedClient(new InetSocketAddress(CACHE_ADDRESS, 11211));
//            MemcachedClient cache = new MemcachedClient(AddrUtil.getAddresses(cacheNodes));
            cache.set(feedAreaName, TIN_CACHE_LIFETIME, rssFeedList);
            log.info("finish saving to cache");
        } catch (Exception e) {
            log.warning("Cannot use cache :(");
            log.warning(e.getLocalizedMessage());
        }
    }

    public void buildCache(List<RssFeed> rssFeeds) {
        log.info("-- start building cache");
        Map<String, List<RssFeed>> map = rssFeeds.stream()
                .collect(Collectors
                        .groupingBy((RssFeed rssFeed) -> rssFeed.getFeedArea().name(), Collectors.toList()));
        for (Map.Entry<String, List<RssFeed>> entry : map.entrySet()) {
            String feedArea = entry.getKey();
            List<RssFeed> rssFeedList = entry.getValue();
            setByFeedArea(feedArea, rssFeedList);
        }
        log.info("-- finish building cache");
    }
}
