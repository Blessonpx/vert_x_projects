import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;

public class NetControl extends AbstractVerticle {
	private final Logger logger = LoggerFactory.getLogger(NetControl.class);
	
	@Override
	public void start(){
		vertx.createNetServer().connectHandler(this::handleClient).listen(3000);
	}

	private void handleClient(NetSocket socket){
		RecordParser.newDelimited("\n",socket) //Parse by looking for new lines.
		.handler(buffer-> handleBuffer(socket,buffer)) // Now buffers are lines.
		.endHandler(v->logger.info("Connection ended"));
	}
	
	
	private void handleBuffer(NetSocket socket,Buffer buffer){
		String command = buffer.toString(); // Buffer-to-string decoding with the default charset
		switch(command){
			case "/list":
				listCommand(socket);
				break;
			case "/play":
				vertx.eventBus().send("jukebox.play","");
				break;
			case "/pause":
				vertx.eventBus().send("jukebox.pause","");
				break;
			default:
				if(command.startsWith("/schedule")){
					schedule(command);
				}else{
					socket.write("Unknown command\n");
				}
		}
	}
	
	private void schedule(String command){
		// The first 10 characters are for /schedule and a space
		String track = command.substring(10);
		JsonObject json = new JsonObject().put("file",track);
		vertx.eventBus().send("jukebox.schedule",json);
	}
	
	
	private void listCommand(NetSocket socket){
		vertx.eventBus().request("jukebox.list","",reply->{
			if(reply.succeeded()){
				JsonObject data =(JsonObject)  reply.result().body();
				data.getJsonArray("files")
				// We write each filename to the standard console output.
				.stream().forEach(name->socket.write(name+"\n"));
			}else{
					logger.error("/list error",reply.cause());
			}
		});
	}
	
	



}
