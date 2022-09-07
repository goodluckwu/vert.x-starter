package com.example.core.sharedata.counter.sender;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.shareddata.Counter;
import io.vertx.core.shareddata.SharedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SenderVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(SenderVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    int port = 8888;
    vertx.createHttpServer().requestHandler(req -> {
      SharedData sharedData = vertx.sharedData();
      sharedData.getCounter("mycounter", res -> {
        if (res.succeeded()) {
          Counter counter = res.result();
          counter.getAndIncrement(resGI -> {
            if (resGI.succeeded()) {
              log.info("SenderVerticle counter getAndIncrement: {}", resGI.result());
            } else {
              log.error("getAndIncrement failed!", resGI.cause());
            }
          });
        } else {
          log.error("getCounter failed!", res.cause());
        }
      });

      sharedData.getLocalCounter("mycounter", res -> {
        if (res.succeeded()) {
          Counter counter = res.result();
          counter.addAndGet(10L, resAddAndGet -> {
            if (resAddAndGet.succeeded()) {
              log.info("SenderVerticle localCounter: {}", resAddAndGet.result());
            } else {
              log.error("SenderVerticle addAndGet failed!", resAddAndGet.cause());
            }
          });
        } else {
          log.error("getCounter failed!", res.cause());
        }
      });


      EventBus eventBus = vertx.eventBus();
      eventBus.request("news.uk.sport", "Yay! Someone kicked a ball", reply -> {
        if (reply.succeeded()) {
          log.info("reply: {}", reply.result().body());
        } else {
          log.error("reply失败！", reply.cause());
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
