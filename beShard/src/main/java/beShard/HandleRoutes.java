package beShard;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;

public class HandleRoutes extends AbstractVerticle {
	private static final Logger logger =  LoggerFactory.getLogger(HandleRoutes.class);
	
	private Router router;
	private WebClient webClient;
	/*
	 * Declare Handlers
	 * 
	 * */
	
	@Override
	public void start() {
		
		router = Router.router(vertx);
		webClient = WebClient.create(vertx);

		vertx.createHttpServer()
		.requestHandler(router)
		.listen(8080);
		
		BodyHandler bodyHandler = BodyHandler.create();
		
		router.post().handler(bodyHandler);
		router.get().handler(bodyHandler);
		
		router.post("/postApi").handler(this::register);
	}
	
	
	private void sendStatusCode(RoutingContext ctx, int code) {
		ctx.response().setStatusCode(code).end();
	}
	
	private void sendBadGateWay(RoutingContext ctx,Throwable err) {
		logger.error("Whoops",err);
		ctx.fail(502);
	}
	
	private void register(RoutingContext ctx) {
		webClient
		.post(3000, "localhost","/postApi")
		.putHeader("Content-Type", "application/json")
		.sendJsonObject(ctx.body().asJsonObject(),ar->{
			if(ar.succeeded()) {
				HttpResponse<Buffer> response = ar.result();
				sendStatusCode(ctx,200);
			}else {
				sendBadGateWay(ctx,ar.cause());
			}
		});
		/*
		 * This converts the request from a Vert.x 
		 * Buffer to a JsonObject.
		 * 
		 * */
	}
}
