import java.util.Random;
import java.util.UUID;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class HeatSensor extends AbstractVerticle{
	private final Random random = new Random();
	private final String sensorId = UUID.randomUUID().toString(); // The sensor identifier is generated using a UUID.
	private double temperature = 21.0;
	
	@Override
	public void start() {
		scheduleNextUpdate();
	}
	
	public void scheduleNextUpdate() {
		// Updates are scheduled with a random delay between one and six seconds.
		vertx.setTimer(random.nextInt(5000)+1000, this::update); 
	}
	public void update(long timerId) {
		temperature = temperature + (delta()/10);
		JsonObject payLoad = new JsonObject()
				.put("id",sensorId)
				.put("temp", temperature);
		// publish sends a message to subscribers.
		vertx.eventBus().publish("sensor.updates", payLoad);
		scheduleNextUpdate();
		// We schedule the next update.
	}
	/*
	 * This computes a random positive or negative value 
	 * to slightly modify the current temperature.
	 * */
	private double delta() {
		if(random.nextInt()>0) {
			return random.nextGaussian();
		}else {
			return -random.nextGaussian();
		}
	}
}
