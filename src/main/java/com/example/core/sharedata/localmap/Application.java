package com.example.core.sharedata.localmap;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new SenderVerticle(), res -> {
      if(res.succeeded()){
        log.info("SenderVerticle Deployment id is: {}", res.result());
      }else {
        log.error("SenderVerticle Deployment failed!", res.cause());
      }
    });
    vertx.deployVerticle(new ReceiverVerticle(), res -> {
      if(res.succeeded()){
        log.info("ReceiverVerticle Deployment id is: {}", res.result());
      }else {
        log.error("ReceiverVerticle Deployment failed!", res.cause());
      }
    });
  }
}
