import io.vertx.core.Vertx;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;

public class ReadViaVertx {
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		OpenOptions opts = new OpenOptions().setRead(true);
		vertx.fileSystem().open("/home/blesson/code/java_files/vert_x_projects/Ch4_Listings/Abc.txt",
				opts, ar->{
					if(ar.succeeded()) {
						/*
						 * AsyncFile is the interface for Vert.x asynchronous files
						 * */
						AsyncFile file = ar.result(); 
						/*
						 * Below Handling is declarative, declaring beforehand 
						 * We are pushed the contents of the file unlike being pulled in case of ReadFileJDK
						 * 
						 * */
						// Callback for new buffer data
						file.handler(System.out::println)  
							.exceptionHandler(Throwable::printStackTrace) // callback when exception arises
							.endHandler(done->{  // callback when the stream ends
								System.out.println("\n ---------- Done");
								vertx.close();
							});
							
					}else {
						ar.cause().printStackTrace();
					}
				});
	}

}
