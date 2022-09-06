package com.example.core.sharedata.localmap;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.shareddata.LocalMap;
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
      LocalMap<Object, Object> mymap1 = sharedData.getLocalMap("mymap1");
      mymap1.put("foo", "bar"); // String是不可变的，所以不需要复制

      LocalMap<Object, Object> mymap2 = sharedData.getLocalMap("mymap2");
      mymap2.put("eek", Buffer.buffer().appendInt(123)); // Buffer将会在添加到Map之前拷贝

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
