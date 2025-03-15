import java.util.HashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;

public class Sensordata extends AbstractVerticle{
	private final Map<String,Double> finalValues = new HashMap<>();
	
	@Override
	public void start() {
		
	}
}
