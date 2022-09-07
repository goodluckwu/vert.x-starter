package com.example.core.sharedata.counter.receiver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.shareddata.Counter;
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

      SharedData sharedData = vertx.sharedData();
      sharedData.getCounter("mycounter", res -> {
        if(res.succeeded()){
          Counter counter = res.result();
          counter.getAndIncrement(resGI -> {
            if(resGI.succeeded()){
              log.info("ReceiverVerticle counter getAndIncrement: {}", resGI.result());
            }else{
              log.error("getAndIncrement failed!", resGI.cause());
            }
          });
        }else {
          log.error("getCounter failed!", res.cause());
        }
      });
      sharedData.getLocalCounter("mycounter", res -> {
        if(res.succeeded()){
          Counter counter = res.result();
          counter.getAndIncrement(resGI -> {
            if(resGI.succeeded()){
              log.info("ReceiverVerticle localCounter: {}", resGI.result());
            }else{
              log.error("getAndIncrement failed!", resGI.cause());
            }
          });
        }else {
          log.error("getCounter failed!", res.cause());
        }
      });


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
  }
}
