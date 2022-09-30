package com.example.web.helloworld;

import com.example.core.standalone.MainVerticle;
import com.example.core.standalone.MainVerticle2;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new HttpVerticle(), res -> {
      if(res.succeeded()){
        log.info("Deployment id is: {}", res.result());
      }else {
        log.error("Deployment failed!", res.cause());
      }
    });
  }
}
