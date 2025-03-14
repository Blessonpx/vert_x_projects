import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

public class MixedThreading extends AbstractVerticle{
	private final Logger logger = LoggerFactory.getLogger("MixedThreading.class");
	
	@Override
	public void start() {
		Context context = vertx.getOrCreateContext();
		new Thread(()->{
				run(context);
		}).start();
	}
	
	
	public void run(Context context) {
        logger.info("I am inside non-vert.x thread..");

        context.runOnContext(v -> {
            logger.info("I am inside vert.x thread..");

            vertx.setTimer(1000, id -> { // ✅ Use setTimer instead of blocking
                logger.info("This is the final Countdown ");
                logger.info("Bye");
            });

            logger.info("Timer scheduled, returning control to event loop.");
        });
    }

	
	/*
	 * Below mentioned run method has latch.await() that throws error because it blocks the event looop
	 * use set timer instead as mentioned above run method
	 *  
	 * */
	
	
/*	public void run(Context context) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		logger.info("I am inside non-vert.x thread..");
		context.runOnContext(v->{
			logger.info("I am inside vert.x thread..");
			vertx.setPeriodic(1000,id->{
				logger.info("This is final Countdown ");
				latch.countDown();
			});
			logger.info("Waiting on the countdown latch...");
			try {
				latch.await();
			}catch(InterruptedException e) {
				logger.error("Whoops ",e);
			}
			logger.info("Bye");
		});
	}
		
*/		
	
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle("MixedThreading");
	}

}
