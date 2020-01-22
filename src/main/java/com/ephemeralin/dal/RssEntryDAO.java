package com.ephemeralin.dal;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class RssEntryDAO {

    private static final String RSS_ENTRIES_TABLE_NAME = System.getenv("RSS_ENTRIES_TABLE_NAME");
    private final Logger log = Logger.getLogger(String.valueOf(this.getClass()));
    private final DynamoDBMapper mapper;
    private final DynamoDBAdapter db_adapter;
    private final AmazonDynamoDB client;

    public RssEntryDAO() {
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(RSS_ENTRIES_TABLE_NAME))
                .build();
        this.db_adapter = DynamoDBAdapter.getInstance();
        this.client = this.db_adapter.getDbClient();
        this.mapper = this.db_adapter.createDbMapper(mapperConfig);
    }

    public List<RssEntry> list() {
        DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
        List<RssEntry> results = this.mapper.scan(RssEntry.class, scanExp);
        for (RssEntry p : results) {
            log.info("RSS Entries - list(): " + p.toString());
        }
        return results;
    }

    public RssEntry get(String id) throws IOException {
        RssEntry rssEntry = null;

        HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
        av.put(":v1", new AttributeValue().withS(id));

        DynamoDBQueryExpression<RssEntry> queryExp = new DynamoDBQueryExpression<RssEntry>()
                .withKeyConditionExpression("id = :v1")
                .withExpressionAttributeValues(av);

        PaginatedQueryList<RssEntry> result = this.mapper.query(RssEntry.class, queryExp);
        if (result.size() > 0) {
            rssEntry = result.get(0);
            log.info("RSS Entries - get(): entry - " + rssEntry.toString());
        } else {
            log.info("RSS Entries - get(): entry - Not Found.");
        }
        return rssEntry;
    }

    public void save(RssEntry rssEntry) throws IOException {
        log.info("RSS Entries - save(): " + rssEntry.toString());
        this.mapper.save(rssEntry);
    }

    public Boolean delete(String id) throws IOException {
        RssEntry rssEntry = null;
        boolean deleted = false;
        rssEntry = get(id);
        if (rssEntry != null) {
            log.info("RSS Entries - delete(): " + rssEntry.toString());
            this.mapper.delete(rssEntry);
            deleted = true;
        } else {
            log.info("RSS Entries - delete(): entry - does not exist.");
        }
        return deleted;
    }
}
