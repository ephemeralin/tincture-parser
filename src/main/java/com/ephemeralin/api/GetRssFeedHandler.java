package com.ephemeralin.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.ephemeralin.dao.RssFeedDAO;
import com.ephemeralin.data.RssFeed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class GetRssFeedHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger log = LogManager.getLogger(this.getClass());
    private RssFeedDAO rssFeedDAO;

    public GetRssFeedHandler() {
        this.rssFeedDAO = RssFeedDAO.getInstance();
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Powered-By", "AWS Lambda & Serverless");
        headers.put("Access-Control-Allow-Origin", "*");
        try {
            Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
            String feedName = pathParameters.get("feedName");
            RssFeed rssFeed = rssFeedDAO.get(feedName);
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(rssFeed)
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