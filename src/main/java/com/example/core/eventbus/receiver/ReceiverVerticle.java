package com.example.core.eventbus.receiver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiverVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(ReceiverVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    EventBus eventBus = vertx.eventBus();
    log.info("eventbus in verticle: {}", eventBus);
    eventBus.consumer("news.uk.sport", message -> {
      log.info("I have received a message: {}", message.body());
      message.reply("how interesting!");
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

    eventBus.localConsumer("news.uk.sport.local", message -> {
      log.info("I have received a message: {}", message.body());
      message.reply("how interesting!");
    });
  }
}
