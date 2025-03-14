import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

public class CreateContext1 extends AbstractVerticle{
	//private final Logger logger = LoggerFactory.getLogger("CreateContext1.class");

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger("CreateContext1.class");
		Vertx vertx = Vertx.vertx();
		Context ctx = vertx.getOrCreateContext();
		ctx.put("foo","bar");
		
		ctx.exceptionHandler(t->{
			if("Tada".equals(t.getMessage())) {
				logger.info("Got_a_Tada_Exception");
			}else {
				logger.error("Woops", t);
			}
		});
		
		ctx.runOnContext(v -> {
			throw new RuntimeException("Tada");
			});
		
		ctx.runOnContext(v -> {
			logger.info("foo = {}", (String) ctx.get("foo"));
			});
	}
}
