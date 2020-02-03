package com.ephemeralin.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;

import java.util.List;

@DynamoDBTable(tableName = "RSS_FEEDS_TABLE_NAME")
public class RssFeed {

    private String feedName;
    private FeedArea feedArea;
    private String feedPrettyName;
    private String feedHostUrl;
    private int feedOrder;
    private List<RssEntry> entries;

    @DynamoDBHashKey
    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    @DynamoDBTypeConvertedEnum
    public FeedArea getFeedArea() {
        return feedArea;
    }

    public void setFeedArea(FeedArea feedArea) {
        this.feedArea = feedArea;
    }

    public String getFeedPrettyName() {
        return feedPrettyName;
    }

    public void setFeedPrettyName(String feedPrettyName) {
        this.feedPrettyName = feedPrettyName;
    }

    public String getFeedHostUrl() {
        return feedHostUrl;
    }

    public void setFeedHostUrl(String feedHostUrl) {
        this.feedHostUrl = feedHostUrl;
    }

    public int getFeedOrder() {
        return feedOrder;
    }

    public void setFeedOrder(int feedOrder) {
        this.feedOrder = feedOrder;
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
                "area ='" + feedArea + '\'' +
                ", entries n.=" + entries.size() +
                '}';
    }
}
