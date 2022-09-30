package com.example.web.helloworld;

import com.hazelcast.client.impl.protocol.task.cache.CacheClearMessageTask;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
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
    router.route().handler(ctx -> {
      HttpServerResponse response = ctx.response();
      response.putHeader("content-type", "text/plain");

      response.end("Hello World from Vert.x-web!");
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
