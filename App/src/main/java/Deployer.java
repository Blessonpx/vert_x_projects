import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class Deployer extends AbstractVerticle{
	private final Logger logger = LoggerFactory.getLogger(Deployer.class);
	
	@Override
	public void start() {
		long delay=1000;
		for(int i=0;i<50;i++) {
			/*
			 * 
			 * We deploy a new instance of EmptyVerticle every second.
			 * 
			 * */
			vertx.setTimer(delay, id-> deploy());
			delay=delay+1000;
		}
	}
	private void deploy() {
		vertx.deployVerticle(new EmptyVerticle(), ar->{
			if(ar.succeeded()) {
				/*
				 * 
				 * Deploying a verticle is an asynchronous operation, and there is a variant of the deploy
				 * method that supports an asynchronous result.
				 * 
				 * */
				String id = ar.result();
				logger.info("Successfully deloyed {}",id);
				vertx.setTimer(5000,tid ->undeploylater(id));// Undeploy the empty verticle after 5 seconds
			}else {
				logger.error("Error while deploying",ar.cause());
			}
		});
	}
	private void undeploylater(String id) {
		vertx.undeploy(id,ar->{
			if(ar.succeeded()){
				logger.info("Successfully undeployed {}",id);
			}else {
				logger.error("Error in undeloying {}",id,ar.cause());
			}
		});
	}
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Deployer());
	}
	
	
}
