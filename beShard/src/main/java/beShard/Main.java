package beShard;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Main {
	public static void main(String[] args) {
		String file_path=args[0];
		
		JsonObject objIn = new JsonObject().put("file",file_path);
		DeploymentOptions opts = new DeploymentOptions().setConfig(objIn);
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new HandleRoutes(),opts,res->{
			if(res.succeeded()) {
				System.out.println("Handles Routes Deployed");
			}else {
				System.err.println("Deployment failed: "+res.cause());
				res.cause().printStackTrace();
			}
		});
	}
}
