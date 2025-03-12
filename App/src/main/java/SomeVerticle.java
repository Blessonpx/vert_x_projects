import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class SomeVerticle extends AbstractVerticle {

	/*
	 * 
	 * how to properly notify the caller of deferred success or failure. A good
	 * example is starting a HTTP server, which is a non-blocking operation.
	 * 
	 * ############################################################
	 * 
	 * There is, however, a problem: some of the operations in a start or a stop method
	 * may be asynchronous, so they may complete after a call to start() or stop() has returned.
	 * 
	 * 
	 * */
	@Override
	public void start(Promise<Void> promise) {
		/*
		 * The Promise is of type void because Vert.x is only interested in the deployment completion, and
		 * there is no value to carry along.
		 * */
		
		vertx.createHttpServer().requestHandler(req->req.response().end("Ok"))
		.listen(8080,ar->{
			/*
			 * The listen variant that supports an asynchronous result indicates wheather the operation failed or succeded 
			 * */
			if(ar.succeeded()) {
				promise.complete();
			}else {
				promise.fail(ar.cause());
			}
		});
	}
	/*
	 * A promise is used to write an asynchronous result, whereas a future is used to view an asynchronous result. 
	 * Given a Promise object, you can call the future() method to obtain a future of type io.vertx.core.Future.
	 * 
	 * */
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new SomeVerticle());
	}
}
