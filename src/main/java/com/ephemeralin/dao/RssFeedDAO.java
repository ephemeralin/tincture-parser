package com.ephemeralin.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
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
    private final Table table;

    public RssFeedDAO() {
        log.info("RssFeedDAO constructor start");
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(RSS_FEEDS_TABLE_NAME))
                .build();
        this.db_adapter = DynamoDBAdapter.getInstance();
        this.client = this.db_adapter.getDbClient();
        this.mapper = this.db_adapter.createDbMapper(mapperConfig);
        this.dynamoDB = new DynamoDB(client);
        this.table = dynamoDB.getTable(RSS_FEEDS_TABLE_NAME);
        log.info("RssFeedDAO constructor done");
    }

    public List<RssFeed> searchByFeedArea(FeedArea feedArea) {
        log.info("start searchByFeedArea " + feedArea.name());
        int numberOfThreads = 4;
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(feedArea.name()));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("feedArea = :val1").withExpressionAttributeValues(eav);
        log.info("scanExpression constructed");
        List<RssFeed> feeds = this.mapper.parallelScan(RssFeed.class, scanExpression, numberOfThreads);
//        List<Product> scanResult = mapper.parallelScan(Product.class, scanExpression, numberOfThreads);
        log.info("mapper scan done");
        log.info("done searchByFeedArea");
        return feeds;
    }

    public void save(RssFeed rssFeed) {
        log.info("RSS Feed - save(): " + rssFeed.toString());
        this.mapper.save(rssFeed);
    }

    public RssFeed get(FeedArea feedArea, String feedName) {
        RssFeed rssFeed = null;
        String feedAreaName = feedArea.name();
        HashMap<String, AttributeValue> av = new HashMap<>();
        av.put(":v1", new AttributeValue().withS(feedAreaName));
        av.put(":v2", new AttributeValue().withS(feedName));
        DynamoDBQueryExpression<RssFeed> queryExp = new DynamoDBQueryExpression<RssFeed>()
                .withKeyConditionExpression("feedArea = :v1 and feedName = :v2")
                .withExpressionAttributeValues(av);
        PaginatedQueryList<RssFeed> result = mapper.query(RssFeed.class, queryExp);
        if (result.size() > 0) {
            rssFeed = result.get(0);
            log.info("RSS Feeds - get(): entry - " + rssFeed.toString());
        } else {
            log.info("RSS Feeds - get(): entry - Not Found.");
        }
        return rssFeed;
    }

    public void saveIfAbsent(RssFeed rssFeed) {
        RssFeed rssFeedExisted = get(rssFeed.getFeedArea(), rssFeed.getFeedName());
        if (rssFeedExisted == null || !rssFeedExisted.equals(rssFeed)) {
            log.info("feed doesn't exist or hashes different. To be save...");
            save(rssFeed);
        } else {
            log.info("feed exist and hashes are the same. Don't need to save...");
        }
    }
}
