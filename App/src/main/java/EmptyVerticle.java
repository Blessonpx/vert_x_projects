import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;

public class EmptyVerticle extends AbstractVerticle {
	private final Logger logger = LoggerFactory.getLogger(EmptyVerticle.class);
	
	
	/*
	 * 
	 * a simple verticle. It does not do anything interesting except logging 
	 * when it starts and stops.
	 * 
	 * */
	
	@Override
	public void start() {
		logger.info("Start");
	}
	
	@Override
	public void stop() {
		logger.info("Stop");
	}
	
	
	
	
}
