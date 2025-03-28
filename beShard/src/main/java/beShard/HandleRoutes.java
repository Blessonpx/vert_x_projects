package beShard;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;

public class HandleRoutes extends AbstractVerticle {
	private static final Logger logger =  LoggerFactory.getLogger(HandleRoutes.class);
	
	/*
	 * Declare Handlers
	 * 
	 * */
	
	@Override
	public void start() {	
		logger.info("HTTP Server started on port");
		WebClient client = WebClient.create(vertx);
		Map<String,String> data = new HashMap<>();
		data.put("name", "Alice");
		data.put("occupation", "Engineer");
		
		/*
		 * First Start the HttpServer
		 * 
		 * */
		
		vertx.createHttpServer()
			.requestHandler(req->{
				req.response().end("Hello from /postApi");
			})
			.listen(8080,http->{
				if(http.succeeded()) {
					logger.info("Http Server started on Port ");
					
					
					client.post(8080,"localhost","/postApi").sendJson(data,response->{
						if(response.succeeded()) {
							HttpResponse<Buffer> httpResponse = response.result();
							logger.info("Sending response {}",httpResponse.statusCode());
							logger.info("Sending json{}",data.toString());
						}else {
							logger.error(response.cause().getMessage());
						}
					});
				}else {
					logger.error("Failed to start HTTP server",http.cause());
				}
			});
		
	}
}
