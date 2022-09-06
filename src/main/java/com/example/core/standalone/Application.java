package com.example.core.standalone;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle(), res -> {
      if(res.succeeded()){
        log.info("Deployment id is: {}", res.result());
      }else {
        log.error("Deployment failed!", res.cause());
      }
    });

    Vertx vertx2 = Vertx.vertx();
    vertx2.deployVerticle(new MainVerticle2(), res -> {
      if(res.succeeded()){
        log.info("Deployment id is: {}", res.result());
      }else {
        log.error("Deployment failed!", res.cause());
      }
    });
  }
}
