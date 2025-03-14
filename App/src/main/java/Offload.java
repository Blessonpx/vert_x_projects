import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class Offload extends AbstractVerticle{
	// Demonstration of how to use ExecuteBlocking
	private final Logger logger = LoggerFactory.getLogger("Offload.class");
	
	@Override
	public void start() {
		vertx.setPeriodic(5000,id->{
			logger.info("Tick");
			vertx.executeBlocking(this::blockingCode,this::resultHandler);
		});
	}
	
	private void blockingCode(Promise<String> promise) {
		try {
			logger.info("Executing Blocking code ...");
			Thread.sleep(4000);
			logger.info("Done!");
			promise.complete("OK!");
		}catch(InterruptedException e ) {
			promise.fail(e);
		}
	}
	
	private void resultHandler(AsyncResult<String> ar) {
		if(ar.succeeded()) {
			logger.info("Result of Blocking code {}",ar.result());
		}else {
			logger.error("Whooops ",ar.cause());
		}
	}
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle("Offload");
	}
}
