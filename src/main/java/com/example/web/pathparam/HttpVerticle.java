package com.example.web.pathparam;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
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

    router.get("/param/:hello").handler(ctx -> {
      ctx.response().end(ctx.pathParam("hello"));
    });

    router
      .get("/flights/:from-:to")
      .handler(ctx -> {
        String from = ctx.pathParam("from"); // AMS
        String to = ctx.pathParam("to"); // SFO
        log.info("from: {}, to: {}", from, to);
        ctx.response().end(from + to);
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
