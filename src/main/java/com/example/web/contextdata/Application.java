package com.example.web.contextdata;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
  private static Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new ContextDataVerticle(), res -> {
      if(res.succeeded()){
        log.info("deploy successfully");
      }else{
        log.error("deploy failed", res.cause());
      }
    });
  }
}
