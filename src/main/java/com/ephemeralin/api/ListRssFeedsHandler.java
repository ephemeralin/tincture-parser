package com.ephemeralin.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.ephemeralin.dao.RssFeedDAO;
import com.ephemeralin.data.RssFeed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListRssFeedsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger log = LogManager.getLogger(this.getClass());
	private RssFeedDAO rssFeedDAO;

	public ListRssFeedsHandler() {
		this.rssFeedDAO = RssFeedDAO.getInstance();
	}

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("X-Powered-By", "AWS Lambda & Serverless");
		headers.put("Access-Control-Allow-Origin", "*");
		try {
			List<RssFeed> rssFeeds = rssFeedDAO.list();
			return ApiGatewayResponse.builder()
					.setStatusCode(200)
					.setObjectBody(rssFeeds)
					.setHeaders(headers)
					.build();
		} catch (Exception ex) {
			log.error("Error in listing RSS feeds: " + ex);
			Response responseBody = new Response("Error in listing RSS feeds: ", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody(responseBody)
					.setHeaders(headers)
					.build();
		}
	}
}