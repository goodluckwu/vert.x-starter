package com.example.core.sharedata.asyncmap.standalone;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.shareddata.AsyncMap;
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
      sharedData.<String, String>getLocalAsyncMap("mymap", res -> {
        if(res.succeeded()){
          AsyncMap<String, String> map = res.result();
          map.get("foo", resGet -> {
            if(resGet.succeeded()){
              log.info("from localAsyncMap foo: {}", resGet.result());
            }else {
              log.error("get failed!", resGet.cause());
            }
          });
          map.get("foo3", resGet -> {
            if(resGet.succeeded()){
              log.info("from localAsyncMap foo3: {}", resGet.result());
            }else {
              log.error("get failed!", resGet.cause());
            }
          });
        }else{
          log.error("getLocalAsyncMap failed!", res.cause());
        }
      });

      sharedData.<String, String>getAsyncMap("mymap", res -> {
        if(res.succeeded()){
          AsyncMap<String, String> map = res.result();
          map.get("foo2", resGet -> {
            if(resGet.succeeded()){
              log.info("from asyncMap foo2: {}", resGet.result());
            }else {
              log.error("get failed!", resGet.cause());
            }
          });
          map.get("foo4", resGet -> {
            if(resGet.succeeded()){
              log.info("from asyncMap foo4: {}", resGet.result());
            }else {
              log.error("get failed!", resGet.cause());
            }
          });
        }else{
          log.error("getAsyncMap failed!", res.cause());
        }
      });
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
