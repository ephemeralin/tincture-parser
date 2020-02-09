package com.ephemeralin.dao;

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
import com.ephemeralin.data.RssFeed;
import com.ephemeralin.util.DynamoDBAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class RssFeedDAO {

    private static final String RSS_FEEDS_TABLE_NAME = System.getenv("RSS_FEEDS_TABLE_NAME");
    private static final String RSS_FEEDS_TABLE_INDEX = System.getenv("RSS_FEEDS_TABLE_INDEX");
    private static final RssFeedDAO instance = new RssFeedDAO();
    private final Logger log = Logger.getLogger(String.valueOf(this.getClass()));
    private final DynamoDBMapper mapper;
    private final DynamoDBAdapter db_adapter;
    private final AmazonDynamoDB client;
    private final DynamoDB dynamoDB;
    private final Table table;

    private RssFeedDAO() {
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

    public List<Item> queryByFeedArea(FeedArea feedArea) {
        log.info("start queryByFeedArea " + feedArea.name());
        Index index = table.getIndex(RSS_FEEDS_TABLE_INDEX);
        QuerySpec querySpec = new QuerySpec();
        log.info("query run start");
        querySpec.withKeyConditionExpression("feedArea = :v_area")
                .withValueMap(new ValueMap().withString(":v_area", feedArea.name()));
        log.info("query run start");
        ItemCollection<QueryOutcome> items = index.query(querySpec);
        log.info("query run done");
        List<Item> resultList = new ArrayList<>();
        for (Item item : items) {
            resultList.add(item);
        }
        log.info("finish queryByFeedArea " + feedArea.name());
        return resultList;
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


    public void warmUp() {
        log.info("start Warming Up...");
        DescribeEndpointsResult describeEndpointsResult = client.describeEndpoints(new DescribeEndpointsRequest());
        List<Endpoint> endpoints = describeEndpointsResult.getEndpoints();
        for (Endpoint endpoint : endpoints) {
            log.info("endpoint: " + endpoint.getAddress());
        }
        RssFeed habr = get("habr");
        log.info("get sample feed: " + habr);
        log.info("...done Warming Up.");
    }

}
