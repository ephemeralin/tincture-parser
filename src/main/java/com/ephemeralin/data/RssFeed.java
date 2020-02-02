package com.ephemeralin.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;

@DynamoDBTable(tableName = "RSS_FEEDS_TABLE_NAME")
public class RssFeed {

    private String feedName;
    private List<RssEntry> entries;

    @DynamoDBHashKey
    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public List<RssEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<RssEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "RssFeed{" +
                "name='" + feedName + '\'' +
                ", entries n.=" + entries.size() +
                '}';
    }
}
