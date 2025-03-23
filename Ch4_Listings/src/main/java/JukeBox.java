import java.io.File;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class JukeBox extends AbstractVerticle {
	/*
	 * Handles all functions related to playing the audio files
	 * list: Reads from track folder and list all mp3 files
	 * play
	 * pause
	 * schedule
	 * httphandler
	 * openAudioStream
	 * download
	 * downloadFile
	 * downloadFilePipe
	 * streamAudioChunk
	 * openNextFile
	 * closeCurrentFile
	 * processReadBuffer
	 * 
	 * */
	private final Logger logger = LoggerFactory.getLogger(JukeBox.class);
	
	
	@Override
	public void start() {
		EventBus eventBus = vertx.EventBus();
		eventBus.consumer("jukebox.list",this::list);
		eventBus.consumer("jukebox.schedule",this::schedule);
		eventBus.consumer("jukebox.play",this::play);
		eventBus.consumer("jukebox.pause",this::pause);
		
		vertx.createHttpServer().requestHandler(this::handler).listen(8080);
		vertx.setPeriodic(100, this::streamAudioChunk);
	}
	
	
	private enum State {PLAYING,PAUSED};
	private State currentmode= State.PAUSED;
	private final Queue<String> playlist =new  ArrayDeque<>();
	
	public void list(Message<?> request){
		vertx.fileSystem().readDir("track", ".*mp3$",ar->{
			// We asynchronously get all the files ending in *.mp3 in track/ folder
			if(ar.succeeded()){
				List<String> files = ar.result()
				.stream()
				.map(File::new)
				.map(File::getName)
				.collect(Collectors.toList());
				
				JsonObject json = new JsonObject().put("files",new JsonArray(files));
				request.reply(json); // We build a JSON response.
			}else{
				// This is an example of sending a failure code and error message in a request/reply communication over the event bus.
				logger.error("readDir failed",ar.cause());
				request.fail(500,ar.cause().getMessage());
			}
		});
	}
	
	public void play(Message<?> request){
		currentmode=State.PLAYING;
	}
	
	public void pause(Message<?> request){
		currentmode=State.PAUSED;
	}
	
	public void schedule(Message<JsonObject> request){
		String file = request.body().getString("file");
		if(playlist.isEmpty() && currentmode==State.PAUSED){
			//	This allows us to automatically resume playing when no track is playing and we schedule a new one
			currentmode=State.PLAYING;
		}
		playlist.offer(file);
	}
	
	public void httpHandler(HttpServerRequest request){
		logger.info("{} '{}' {}", request.method(), request.path(), request.remoteAddress());
		if("/".equals(request.path())){
			openAudioStream(request);
			return;
		}
		if(request.path().startsWith("/download/")){
			// This string substitution prevents malicious attempts to read files from other directories (think of  someone willing to read /etc/passwd).
			
			String sanitizedPath = request.path().substring(10).replaceAll("/","");
			download(sanitizedPath,request);
			return;
		}
		request.response().setStatusCode(404).end();
	}
	
	private final Set<HttpServerResponse> streamers = new HashSet<>();
	
	private void openAudioStream(HttpServerRequest request){
		logger.info("New streamer");
		HttpServerResponse response = request.response()
				.putHeader("Content-Type","audio/mpeg")
				// It is a stream, so the length is unknown.
				.setChunked(true);
		streamers.add(response);
		
		response.endHandler(v->{
			streamers.remove(response);
			logger.info("A streamer left"); // When a stream exits, it is no longer tracked.
		});
	}
	
	private void download(String path ,HttpServerRequest request){
		String file ="tracks/"+path;
		// Unless you are on a networked filesystem, the possible blocking time is marginal, so we avoid a nested callback level.
		if(!vertx.fileSystem().existsBlocking(file)){
				request.response().setStatusCode(404).end();
				return ;
		}
		OpenOptions opts =new  OpenOptions().setRead(true);
		vertx.fileSystem().open(file,opts,ar->{
			if(ar.succeeded()){
				downloadFile(ar.result(),request);
			}else{
				logger.error("Read Failed",ar.cause());
				request.response().setStatusCode(500).end();
			}
		});
}
	
	
	
}
