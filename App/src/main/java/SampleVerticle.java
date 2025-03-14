import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class SampleVerticle extends AbstractVerticle{
	private final Logger logger = LoggerFactory.getLogger(SampleVerticle.class);
	
	
	/*
	 * Checking how configuration is passed among verticles
	 * */
	
	@Override
	public void start() {
		/*
		 * config() returns the JsonObject configuration instance, and the accessor method supports 
		 * optional default values. Here, if there is no “n” key in the JSON object, -1 is returned.
		 * 
		 * */
		logger.info("n= {}",config().getInteger("n",-1));
	}
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		for(int i=0;i<4;i++) {
			JsonObject config = new JsonObject().put("n", i);// We create a JSON
			/*
			 * The DeploymentOption allows more control on a verticle, including
			 * passing configuration data
			 * 
			 * */
			DeploymentOptions opts = new DeploymentOptions().setConfig(config).setInstances(i);// Can deploy multiple instances at once 
			vertx.deployVerticle("SampleVerticle",opts);
			/*
			 * Since we deploy multiple instances, we need to point to the verticle  
			 * using its fully qualified class name (FQCN) rather than using the new operator. 
			 * For deploying just one instance, you can elect either an instance created with new or using a FQCN.
			 * 
			 * */
		}
		 
	}

}
