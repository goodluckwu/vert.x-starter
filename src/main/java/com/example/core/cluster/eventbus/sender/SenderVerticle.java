package com.example.core.cluster.eventbus.sender;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SenderVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(SenderVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    int port = 8888;
    vertx.createHttpServer().requestHandler(req -> {
      EventBus eventBus = vertx.eventBus();
      eventBus.request("news.uk.sport", "Yay! Someone kicked a ball", ar -> {
        if (ar.succeeded()) {
          log.info("Reply message: {}", ar.result().body());
        }
      });
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Sender had send the message!");
    }).listen(port, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        log.info("HTTP server started on port {}", port);
      } else {
        log.error("HTTP server start failed", http.cause());
        startPromise.fail(http.cause());
      }
    });
  }
}
