package com.ephemeralin.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.ephemeralin.dao.RssFeedDAO;
import com.ephemeralin.data.FeedArea;
import com.ephemeralin.data.RssFeed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchRssFeedsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger log = LogManager.getLogger(this.getClass());
    private RssFeedDAO rssFeedDAO;

    public SearchRssFeedsHandler() {
        this.rssFeedDAO = RssFeedDAO.getInstance();
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        log.info("start Handle request");
        log.info("input: " + input);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Powered-By", "AWS Lambda & Serverless");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");
        try {
            Map<String, String> queryStringParameters = (Map<String, String>) input.get("queryStringParameters");
            FeedArea feedArea = FeedArea.valueOf(queryStringParameters.get("feedArea").toString());
            log.info("feed area object: " + feedArea.toString());
            log.info("get ready to run: rssFeedDAO.searchByFeedArea(feedArea)");
            List<RssFeed> rssFeeds = rssFeedDAO.searchByFeedArea(feedArea);
            log.info("done: rssFeedDAO.searchByFeedArea(feedArea)");
            List<RssFeed> sortedRssFeeds = rssFeeds.stream().sorted(Comparator.comparingInt(RssFeed::getFeedOrder)).collect(Collectors.toList());
            log.info("feeds sorted");
            log.info("feeds:");
            log.info(sortedRssFeeds);
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(sortedRssFeeds)
                    .setHeaders(headers)
                    .build();
        } catch (Exception ex) {
            log.error("Error in retrieving RSS Feed: " + ex);
            Response responseBody = new Response("Error in retrieving RSS Feed: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(headers)
                    .build();
        }
    }
}