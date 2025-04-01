package beShard;

import io.vertx.core.Vertx;

public class Main {
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new HandleRoutes(),res->{
			if(res.succeeded()) {
				System.out.println("Handles Routes Deployed");
			}else {
				System.err.println("Deployment failed: "+res.cause());
				res.cause().printStackTrace();
			}
		});
	}
}
