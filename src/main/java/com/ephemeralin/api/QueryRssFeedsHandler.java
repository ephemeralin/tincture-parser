package com.ephemeralin.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.ephemeralin.data.FeedArea;
import com.ephemeralin.data.RssFeed;
import com.ephemeralin.service.RssFeedDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryRssFeedsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static RssFeedDAO rssFeedDAO = RssFeedDAO.getInstance();
    private final Logger log = LogManager.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        log.info("start Handle Query request");
        log.info("input: " + input);
        if (input.containsKey("source") && input.get("source").equals("serverless-plugin-warmup")) {
            return handleWarmUpRequest();
        }
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Powered-By", "AWS Lambda & Serverless");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");
        try {
            Map<String, String> queryStringParameters = (Map<String, String>) input.get("queryStringParameters");
            FeedArea feedArea = FeedArea.valueOf(queryStringParameters.get("feedArea").toString());
            log.info("feed area object: " + feedArea.toString());
            log.info("get ready to run: rssFeedDAO.queryByFeedArea(feedArea)");
            List<RssFeed> sortedRssFeeds = rssFeedDAO.queryByFeedArea(feedArea);
            log.info("done: rssFeedDAO.queryByFeedArea(feedArea)");
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

    private ApiGatewayResponse handleWarmUpRequest() {
        try {
            RssFeed habr = rssFeedDAO.get("habr");
        } catch (Exception ex) {
            log.info("WarmUP - cannot get rss feed");
        }
        log.info("WarmUP - Search RSS Lambda is warm!");
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setRawBody("WarmUp - Search RSS Lambda is warm!")
                .build();
    }
}