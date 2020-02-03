package com.ephemeralin.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.ephemeralin.data.FeedArea;
import com.ephemeralin.data.RssFeed;
import com.ephemeralin.util.DynamoDBAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class RssFeedDAO {

    private static final String RSS_FEEDS_TABLE_NAME = System.getenv("RSS_FEEDS_TABLE_NAME");
    private static final RssFeedDAO instance = new RssFeedDAO();
    private final Logger log = Logger.getLogger(String.valueOf(this.getClass()));
    private final DynamoDBMapper mapper;
    private final DynamoDBAdapter db_adapter;
    private final AmazonDynamoDB client;
    private final DynamoDB dynamoDB;

    private RssFeedDAO() {
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(RSS_FEEDS_TABLE_NAME))
                .build();
        this.db_adapter = DynamoDBAdapter.getInstance();
        this.client = this.db_adapter.getDbClient();
        this.mapper = this.db_adapter.createDbMapper(mapperConfig);
        this.dynamoDB = new DynamoDB(client);
    }

    public static RssFeedDAO getInstance() {
        return instance;
    }

    public List<RssFeed> list() {
        DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
        List<RssFeed> results = this.mapper.scan(RssFeed.class, scanExp);
        for (RssFeed p : results) {
            log.info("RSS Feed entries - list(): " + p.toString());
        }
        return results;
    }

    public RssFeed get(String feedName) {
        RssFeed rssFeed = null;
        HashMap<String, AttributeValue> av = new HashMap<>();
        av.put(":v1", new AttributeValue().withS(feedName));
        DynamoDBQueryExpression<RssFeed> queryExp = new DynamoDBQueryExpression<RssFeed>()
                .withKeyConditionExpression("feedName = :v1")
                .withExpressionAttributeValues(av);
        PaginatedQueryList<RssFeed> result = this.mapper.query(RssFeed.class, queryExp);
        if (result.size() > 0) {
            rssFeed = result.get(0);
            log.info("RSS Feeds - get(): entry - " + rssFeed.toString());
        } else {
            log.info("RSS Feeds - get(): entry - Not Found.");
        }
        return rssFeed;
    }

    public List<RssFeed> searchByFeedArea(FeedArea feedArea) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(feedArea.name()));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("feedArea = :val1").withExpressionAttributeValues(eav);
        return this.mapper.scan(RssFeed.class, scanExpression);
    }

    public void save(RssFeed rssEntry) {
        log.info("RSS Feed - save(): " + rssEntry.toString());
        this.mapper.save(rssEntry);
    }

    public boolean deleteByName(String name) {
        boolean deleted = false;
        Object rssFeed = get(name);
        if (rssFeed != null) {
            log.info("RSS Feeds - delete(): " + rssFeed.toString());
            this.mapper.delete(rssFeed);
            deleted = true;
        } else {
            log.info("RSS Feeds - delete(): entry - does not exist.");
        }
        return deleted;
    }


}
