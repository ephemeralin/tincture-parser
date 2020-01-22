package com.ephemeralin;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.ephemeralin.dal.RssEntry;
import com.ephemeralin.dal.RssEntryDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ListRssEntriesHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger log = LogManager.getLogger(this.getClass());
	private RssEntryDAO rssEntryDAO;

	public ListRssEntriesHandler() {
		this.rssEntryDAO = RssEntryDAO.getInstance();
	}

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
//			 get all entries
			List<RssEntry> products = rssEntryDAO.list();

			log.info("test OK");

			// send the response back
			return ApiGatewayResponse.builder()
					.setStatusCode(200)
					.setObjectBody(products)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
					.build();
		} catch (Exception ex) {
			log.info("test error");
			log.error("Error in listing products: " + ex);

			// send the error response back
			Response responseBody = new Response("Error in listing RSS entries: ", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
					.build();
		}
	}
}