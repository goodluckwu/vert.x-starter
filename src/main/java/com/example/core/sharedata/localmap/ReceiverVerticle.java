package com.example.core.sharedata.localmap;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.shareddata.SharedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiverVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(ReceiverVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    EventBus eventBus = vertx.eventBus();
    eventBus.consumer("news.uk.sport", message -> {
      log.info("I have received a message: {}", message.body());
      message.reply("how interesting!");

      SharedData sharedData = vertx.sharedData();
      log.info("from shareData mymap1: {}", sharedData.getLocalMap("mymap1").get("foo"));
      log.info("from shareData mymap2: {}", ((Buffer) sharedData.getLocalMap("mymap2").get("eek")).getInt(0));
    }).completionHandler(res -> {
      if(res.succeeded()){
        log.info("The handler registration has reached all nodes");
        startPromise.complete();
        log.info("receiver started");
      }else {
        log.error("Registration failed!");
        startPromise.fail(res.cause());
      }
    });
  }
}
