package com.example.web.metadata;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MetadataVerticle(), res -> {
      if(res.failed()){
        log.error("deploy failed", res.cause());
      }
    });
  }
}
