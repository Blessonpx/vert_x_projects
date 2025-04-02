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
			StringBuilder responseBuilder = new StringBuilder();
			logger.info(responseBuilder.toString());
			
			ctx.queryParams().forEach(entry->{
				responseBuilder.append(entry.getKey())
					.append("=")
					.append(entry.getValue())
					.append("&");
			});
			
			if(responseBuilder.length()>0) {
				responseBuilder.setLength(responseBuilder.length()-1);
			}
			
			

			String url="/ss/"+name;
			if(responseBuilder.length()>0) {
				url+="?"+responseBuilder;
			}
			
			logger.info(url);
			ctx.response()
			.setStatusCode(301)
			.putHeader("Location", url)
			.end();
		});
		
		
		router.get("/ss/:param").handler(ctx->{
			logger.info("Inside /ss/:param");
			String param=ctx.pathParam("param");
			StringBuilder responseBuilder = new StringBuilder(param);
			
			ctx.queryParams().forEach(entry->{
				responseBuilder.append(entry.getKey())
                .append("=")
                .append(entry.getValue())
                .append("\n");
			});
			ctx.response()
				.putHeader("Content-Type","text/plain")
				.send(responseBuilder.toString());
		});
		
		
		
		vertx.createHttpServer()
		.requestHandler(router).listen(8080);
	}
}
