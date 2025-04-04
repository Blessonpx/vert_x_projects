import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class Listener extends AbstractVerticle{
	private final Logger logger = LoggerFactory.getLogger("Listener.class");
	private final DecimalFormat format = new DecimalFormat("#.##");
	// We don’t need the full double value, so we format all temperatures to two-decimal string representations.
	
	@Override 
	public void start() {
		EventBus bus = vertx.eventBus();
		/*
		 * The consumer method allows subscribing to messages, and a callback handles all event-bus messages.
		 * 
		 * */
		bus.<JsonObject>consumer("sensor.updates",msg->{
			// Message payload stored in the body 
			JsonObject body = msg.body();
			String id = body.getString("id");
			String temperature = format.format(body.getDouble("temp"));
			logger.info("{} reports a temperature ~{}C", id, temperature);
		});
	}
}
