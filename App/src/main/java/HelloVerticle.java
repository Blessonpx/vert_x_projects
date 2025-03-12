import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class HelloVerticle extends AbstractVerticle{
	private final Logger logger = LoggerFactory.getLogger(HelloVerticle.class);
	private long counter=1;
	
	@Override
	public void start() {
		/*
		 * This defines a periodic 
		 * task every five seconds.
		 * 
		 * */
		vertx.setPeriodic(5000,id->{
			logger.info("tick");
		});
		
		/*
		 * The HTTP server calls this
		 * handler on every request.
		 * 
		 * */
		vertx.createHttpServer().requestHandler(req->{
			logger.info("Request #{} from {}",counter++,
					req.remoteAddress().host());
			req.response().end("Hello!");
		})
		.listen(8080);
		logger.info("Open http://localhost:8080/");
		
	}
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx(); // We need a global Vert.x instance.
		vertx.deployVerticle(new HelloVerticle());  // This is the simplest way to deploy a verticle.
		/*
		 * 
		 * POINT to REMEMBER
		 * event processing happens on a single event-loop thread. Both the periodic 
		 * tasks and HTTP request processing happen on a thread that appears as vert.x 
		 * -eventloop-thread-0 in the logs. 
		 *  
		 *  
		 *  In a multithreaded design, updating the counter field would require 
		 *  either a synchronized block or the use of java.util.concurrent.AtomicLong. There 
		 *  is no such issue here, so a plain long field can be safely used.
		 * 
		 * 
		 * 
		 * */
	}
}
