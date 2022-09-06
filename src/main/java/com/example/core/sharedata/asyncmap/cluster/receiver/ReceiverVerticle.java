package com.example.core.sharedata.asyncmap.cluster.receiver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
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

      SharedData sharedData = vertx.sharedData();
      sharedData.<String, String>getAsyncMap("mymap", res -> {
        if(res.succeeded()){
          res.result().get("foo", resGet -> {
            if(resGet.succeeded()){
              log.info("from asyncMap foo: {}", resGet.result());
            }else{
              log.error("get foo failed!");
            }
          });
          res.result().get("foo3", resGet -> {
            if(resGet.succeeded()){
              log.info("from asyncMap foo3: {}", resGet.result());
            }else{
              log.error("get foo3 failed!");
            }
          });
        }else{
          log.error("getAsyncMap error!", res.cause());
        }
      });

      sharedData.<String, String>getLocalAsyncMap("mymap", res -> {
        if(res.succeeded()){
          res.result().get("foo2", resGet -> {
            if(resGet.succeeded()){
              log.info("from localAsyncMap foo2: {}", resGet.result());
            }else{
              log.error("get foo2 " +
                "failed!");
            }
          });
          res.result().get("foo4", resGet -> {
            if(resGet.succeeded()){
              log.info("from localAsyncMap foo4: {}", resGet.result());
            }else{
              log.error("get foo4 failed!");
            }
          });
        }else{
          log.error("getAsyncMap error!", res.cause());
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
