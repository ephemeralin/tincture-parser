package com.ephemeralin.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;

import java.util.logging.Logger;

@DynamoDBDocument
public class RssEntry {

    private final Logger log = Logger.getLogger(String.valueOf(this.getClass()));

    private String title;
    private String description;
    private String url;
    private FeedSource feedSource;

    public RssEntry() {
    }

    public RssEntry(String title, String description, String url, FeedSource feedSource) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.feedSource = feedSource;
    }

    //    @DynamoDBRangeKey(attributeName = "title")
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //    @DynamoDBAttribute(attributeName="description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //    @DynamoDBAttribute(attributeName="url")
//    @DynamoDBHashKey(attributeName = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //    @DynamoDBAttribute(attributeName="feed_source")
    @DynamoDBTypeConvertedEnum
    public FeedSource getFeedSource() {
        return this.feedSource;
    }

    public void setFeedSource(FeedSource feedSource) {
        this.feedSource = feedSource;
    }

    @Override
    public String toString() {
        return "RssEntry{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", source=" + feedSource +
                '}';
    }
}