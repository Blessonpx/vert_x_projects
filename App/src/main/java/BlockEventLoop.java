import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class BlockEventLoop extends AbstractVerticle{
	
	
	/*
	 * 
	 * This is to show what running thread blocking events from the same event can do
	 * 
	 * Thread gets blocked and throws an error 
	 * 
	 * BlockedThreadChecker throws an error every 2 seconds
	 * */
	
	@Override
	public void start(){
		vertx.setTimer(1000, id ->{
			while(true);
		});
	}
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new BlockEventLoop());
	}

}
