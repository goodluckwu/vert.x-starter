package com.example.web.block;

import java.util.concurrent.TimeUnit;
import com.hazelcast.internal.util.TimeUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(HttpVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    final int port = 8080;
    HttpServer httpServer = vertx.createHttpServer();
    Router router = Router.router(vertx);

    router.get("/block").blockingHandler(ctx -> {
      try {
        TimeUnit.SECONDS.sleep(10L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      ctx.response().end(Thread.currentThread().toString());
    });

    httpServer.requestHandler(router).listen(port, http -> {
      if (http.succeeded()) {
        log.info("HTTP server started on port {}", port);
        startPromise.complete();
      } else {
        log.error("HTTP server start failed", http.cause());
        startPromise.fail(http.cause());
      }
    });
  }
}
