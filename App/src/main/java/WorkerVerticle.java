import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class WorkerVerticle extends AbstractVerticle {
	private final Logger logger = LoggerFactory.getLogger(WorkerVerticle.class);
	
	@Override
	public void start() {
		vertx.setPeriodic(10_000,id->{
			try {
				logger.info("Zzz..");
				Thread.sleep(8000);// This wont throw up a blocking error 
				logger.info("Up!");
			}catch(InterruptedException e) {
				logger.error("Woops", e);
			}
		} );
	}
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		// making a Worker verticle is a deployment options Flag
		DeploymentOptions opts = new DeploymentOptions().setInstances(2).setWorker(true);
		vertx.deployVerticle("WorkerVerticle",opts);
	}

}
