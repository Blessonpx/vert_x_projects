import io.vertx.core.AbstractVerticle;
import io.vertx.core.TimeoutStream;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;

public class HttpServer extends AbstractVerticle{
	
	@Override
	public void start() {
		vertx.createHttpServer()
		.requestHandler(this::handler)
		// The HTTP server port is configured with 8080 as the default value.
		.listen(config().getInteger("port",8081));
	}
	
	private void handler(HttpServerRequest request) {
		if("/".equals(request.path())) {
			System.out.println("Serving index.html file");
			request.response().sendFile("target/classes/index.html");
			/*
			 * The sendFile method allows the content of any local file 
			 * to be streamed to the client. This closes the connection 
			 * automatically.
			 * 
			 * */
		}else if("/sse".equals(request.path())) {
			/*
			 * Server-sent events will use the /sse resource, and we provide
			 * a method for handling these requests.
			 * 
			 * */
			sse(request);
		}else{
			request.response().setStatusCode(404);
			/*
			 * Anything else triggers an HTTP 404 (not found) response.
			 * 
			 * */
		}
	}
	
	private void sse(HttpServerRequest request) {
		HttpServerResponse response = request.response();
		response
		.putHeader("Content-Type", "text/event-stream")
		.putHeader("Cache-Control","no-cache")
		.setChunked(true);
		
		MessageConsumer<JsonObject> consumer = 
				vertx.eventBus().consumer("sensor.updates");
		
		consumer.handler(msg->{
			response.write("event: update \n");
			response.write("data:"+ msg.body().encode() + "\n\n");
		});
		
	    // ✅ Use setPeriodic() instead of TimeoutStream
	    long timerId = vertx.setPeriodic(1000, id -> {
	        vertx.eventBus().<JsonObject>request("sensor.average", "",
	            reply -> {
	                if (reply.succeeded()) {
	                    response.write("event: average \n");
	                    response.write("data:" + reply.result().body().encode() + "\n\n");
	                }
	            });
	    });

	    // Proper cleanup when client disconnects
	    response.endHandler(v -> {
	        consumer.unregister();
	        vertx.cancelTimer(timerId); // ✅ Cancel the timer using its ID
	    });
		
		
		
//		TimeoutStream ticks = vertx.periodicStream(1000);
//		
//		ticks.handler(id->{
//			vertx.eventBus().<JsonObject>request("sensor.average","",
//					reply->{
//						if(reply.succeeded()) {
//							response.write("event: average \n");
//							response.write("data:"+reply.result().body().encode()+ "\n\n");
//						}
//					});
//		});
//		
//		response.endHandler(v->{
//			consumer.unregister();
//			ticks.cancel();
//		});
		
	}
}
