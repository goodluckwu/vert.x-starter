package com.example.web.validation;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new ValidationVerticle(), res -> {
      if(res.succeeded()){
        log.info("{} had deployed!", res.result());
      } else {
        log.error("deploy failed", res.cause());
      }
    });
  }
}
