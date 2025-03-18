import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Main {
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle("HeatSensor",new DeploymentOptions().setInstances(4));
		vertx.deployVerticle(new Listener());
		vertx.deployVerticle(new Sensordata());
		vertx.deployVerticle(new HttpServer());
	}

}
