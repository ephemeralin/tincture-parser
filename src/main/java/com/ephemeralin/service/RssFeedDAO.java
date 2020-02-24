package com.ephemeralin.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DescribeEndpointsRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeEndpointsResult;
import com.amazonaws.services.dynamodbv2.model.Endpoint;
import com.ephemeralin.data.FeedArea;
import com.ephemeralin.data.RssEntry;
import com.ephemeralin.data.RssFeed;
import com.ephemeralin.util.DynamoDBAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RssFeedDAO {

    public static final String RSS_FEEDS_TABLE_NAME = System.getenv("RSS_FEEDS_TABLE_NAME");
    public static final RssFeedDAO instance = new RssFeedDAO();
    private static final Logger log = Logger.getLogger(String.valueOf(RssFeedDAO.class));
    private DynamoDBMapper dbMapper;
    private DynamoDBAdapter dbAdapter;
    private AmazonDynamoDB dbClient;
    private DynamoDB dynamoDB;
    private Table dbTable;

    private RssFeedDAO() {
        log.info("RssFeedDAO constructor start");
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(RSS_FEEDS_TABLE_NAME))
                .build();
        this.dbAdapter = DynamoDBAdapter.getInstance();
        this.dbClient = dbAdapter.getDbClient();
        this.dbMapper = dbAdapter.createDbMapper(mapperConfig);
        this.dynamoDB = new DynamoDB(dbClient);
        this.dbTable = dynamoDB.getTable(RSS_FEEDS_TABLE_NAME);
        log.info("RssFeedDAO constructor done");
    }

    public static RssFeedDAO getInstance() {
        return instance;
    }

    public List<RssFeed> list() {
        DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
        List<RssFeed> results = dbMapper.scan(RssFeed.class, scanExp);
        log.info("RSS Feed entries - list() size: " + results.size());
        return results;
    }

    public RssFeed get(String feedName) {
        RssFeed rssFeed = null;
        HashMap<String, AttributeValue> av = new HashMap<>();
        av.put(":v1", new AttributeValue().withS(feedName));
        DynamoDBQueryExpression<RssFeed> queryExp = new DynamoDBQueryExpression<RssFeed>()
                .withKeyConditionExpression("feedName = :v1")
                .withExpressionAttributeValues(av);
        PaginatedQueryList<RssFeed> result = dbMapper.query(RssFeed.class, queryExp);
        if (result.size() > 0) {
            rssFeed = result.get(0);
            log.info("RSS Feeds - get(): entry - " + rssFeed.toString());
        } else {
            log.info("RSS Feeds - get(): entry - Not Found.");
        }
        return rssFeed;
    }

    public List<RssFeed> queryByFeedArea(FeedArea feedArea) {
        log.info("start queryByFeedArea " + feedArea.name());
        log.info("query run start");
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("feedArea = :v_area")
                .withValueMap(new ValueMap().withString(":v_area", feedArea.name()));
        log.info("query run start");
        ItemCollection<QueryOutcome> items = dbTable.query(querySpec);
        log.info("query run done");
        List<RssFeed> rssFeeds = new ArrayList<>();
        for (Item item : items) {
            RssFeed rssFeed = convertItemToRssFeed(item);
            rssFeeds.add(rssFeed);
        }
        log.info("demarshalled to RssFeeds list");
        List<RssFeed> sortedRssFeeds = rssFeeds.stream().sorted(Comparator.comparingInt(RssFeed::getFeedOrder)).collect(Collectors.toList());
        log.info("sorted RssFeeds list");
        log.info("finish queryByFeedArea " + feedArea.name());
        return sortedRssFeeds;
    }

    public void save(RssFeed rssFeed) {
        log.info("RSS Feed - save(): " + rssFeed.toString());
        this.dbMapper.save(rssFeed);
    }

    public boolean deleteByName(String name) {
        boolean deleted = false;
        Object rssFeed = get(name);
        if (rssFeed != null) {
            log.info("RSS Feeds - delete(): " + rssFeed.toString());
            this.dbMapper.delete(rssFeed);
            deleted = true;
        } else {
            log.info("RSS Feeds - delete(): entry - does not exist.");
        }
        return deleted;
    }

    private RssFeed convertItemToRssFeed(Item item) {
        RssFeed rssFeed = new RssFeed();
        rssFeed.setFeedOrder(item.getInt("feedOrder"));
        rssFeed.setFeedHostUrl(item.getString("feedHostUrl"));
        rssFeed.setFeedPrettyName(item.getString("feedPrettyName"));
        rssFeed.setFeedArea(FeedArea.valueOf(item.getString("feedArea")));
        rssFeed.setFeedName(item.getString("feedName"));
        rssFeed.setFeedUpdated(item.getString("feedUpdated"));
        List<HashMap<String, String>> entries = item.getList("entries");
        ArrayList<RssEntry> rssEntries = new ArrayList<>();
        for (HashMap<String, String> entry : entries) {
            RssEntry rssEntry = new RssEntry();
            rssEntry.setDescription(entry.get("description"));
            rssEntry.setTitle(entry.get("title"));
            rssEntry.setUrl(entry.get("url"));
            rssEntries.add(rssEntry);
        }
        rssFeed.setEntries(rssEntries);
        return rssFeed;
    }

    public void warmUp() {
        log.info("start Warming Up...");
        DescribeEndpointsResult describeEndpointsResult = dbClient.describeEndpoints(new DescribeEndpointsRequest());
        List<Endpoint> endpoints = describeEndpointsResult.getEndpoints();
        for (Endpoint endpoint : endpoints) {
            log.info("endpoint: " + endpoint.getAddress());
        }
        RssFeed habr = get("habr");
        log.info("get sample feed: " + habr);
        log.info("...done Warming Up.");
    }
}
