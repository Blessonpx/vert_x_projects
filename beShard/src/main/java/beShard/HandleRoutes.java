package beShard;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public class HandleRoutes extends AbstractVerticle {
	private static final Logger logger =  LoggerFactory.getLogger(HandleRoutes.class);
	
	/*
	 * Declare Handlers
	 * 
	 * */
	
	@Override
	public void start() {
		Router router = Router.router(vertx);
		logger.info("HTTP Server started on port");
		router.get("/").handler(ctx->{
			ctx.response().send("you passed ");
		});
		
		router.get("/hello/:param").handler(ctx->{
			logger.info("Inside /hello/:param");
			String name = ctx.pathParam("param");
			StringBuilder responseBuilder = new StringBuilder("Path:"+name+"\n QueryParams: \n");
			
			ctx.request().params().forEach(entry->{
				responseBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
			});
			
			ctx.response().putHeader("Content-Type","text/plain").end(responseBuilder.toString());
		});
		
		
		
		
		vertx.createHttpServer()
		.requestHandler(router).listen(8080);
	}
}
