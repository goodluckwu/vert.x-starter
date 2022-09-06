package com.example.core.sharedata.asyncmap.cluster.sender;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.shareddata.AsyncMap;
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
      sharedData.<String, String>getAsyncMap("mymap", res -> {
        if(res.succeeded()){
          AsyncMap<String, String> map = res.result();
          map.put("foo", "bar", resPut -> {
            if(resPut.succeeded()){
              log.info("getAsyncMap put foo succeed");
            }else{
              log.error("getAsyncMap put foo failed!");
            }
          });

          map.put("foo2", "bar2", resPut -> {
            if(resPut.succeeded()){
              log.info("getAsyncMap put foo2 succeed");
            }else{
              log.error("getAsyncMap put foo2 failed!");
            }
          });
        }else{
          log.error("getAsyncMap error!", res.cause());
        }
      });

      sharedData.<String, String>getLocalAsyncMap("mymap", res -> {
        if(res.succeeded()){
          AsyncMap<String, String> map = res.result();
          map.put("foo3", "bar3", resPut -> {
            if(resPut.succeeded()){
              log.info("getLocalAsyncMap put foo3 succeed");
            }else{
              log.error("getLocalAsyncMap put foo3 failed!");
            }
          });
          map.put("foo4", "bar4", resPut -> {
            if(resPut.succeeded()){
              log.info("getLocalAsyncMap put foo4 succeed");
            }else{
              log.error("getLocalAsyncMap put foo4 failed!");
            }
          });
        }else{
          log.error("getLocalAsyncMap error!", res.cause());
        }
      });

      EventBus eventBus = vertx.eventBus();
      eventBus.send("news.uk.sport", "Yay! Someone kicked a ball");
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
