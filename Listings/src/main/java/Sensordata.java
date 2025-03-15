import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class Sensordata extends AbstractVerticle{
	// We store the latest measurement of each sensor by its unique identifier.
	private final Map<String,Double> finalValues = new HashMap<>();
	
	@Override
	public void start() {
		EventBus bus = vertx.eventBus();
		bus.consumer("sensor.updates",this::update);
		bus.consumer("sensor.average",this::average);
	}
	private void update(Message<JsonObject> message) {
		JsonObject body= message.body();
		finalValues.put(body.getString("id"), body.getDouble("temp"));
	}
	private void average(Message<JsonObject> message) {
		double avg=finalValues.values().stream()
				.collect(Collectors.averagingDouble(Double::doubleValue));
		JsonObject json=new JsonObject().put("avg",avg);
		message.reply(json);
	}
}
