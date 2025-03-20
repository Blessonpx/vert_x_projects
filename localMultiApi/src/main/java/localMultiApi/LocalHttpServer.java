package localMultiApi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

public class LocalHttpServer extends AbstractVerticle{
	private static final Logger logger = LogManager.getLogger(LocalHttpServer.class);
	
	
	
	/*
	 * Http serrver
	 * does multi routing creating 2 apis 
	 * */
	@Override
	public void start() {
		Router router = Router.router(vertx);
		
		router.route("/static/*").handler(StaticHandler.create("webroot"));
		
		router.get("/").handler(ctx->{
			System.out.println("Serving '/' endpoint ");
			ctx.response().send("Hi");
		});
		
		router.get("/screen_").handler(ctx->{
			ctx.response().sendFile("webroot/index.html");
		});
		
		router.get("/api1/:param").handler(this::handleApi1);
		
		vertx.createHttpServer()
		.requestHandler(router)
		.listen(8080);
	}
	
	private void handleApi1(RoutingContext ctx) {
		String param= ctx.pathParam("param");
		System.out.println("Recieved Param"+param);
		logger.info("Recieved Param"+param);
		ctx.response().end("You have sent param: "+param);
	}
	
	
}
