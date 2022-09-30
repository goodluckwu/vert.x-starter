package com.example.web.next;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
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
    Route route = router.route("/next");

    route.handler(ctx -> {
      HttpServerResponse response = ctx.response();
      response.setChunked(true);
      response.write("route1\n");

      ctx.vertx().setTimer(5000, tid -> ctx.next());
    });
    route.handler(ctx -> {
      HttpServerResponse response = ctx.response();
      response.write("route2\n");

      ctx.vertx().setTimer(5000, tid -> ctx.next());
    });
    route.handler(ctx -> {
      HttpServerResponse response = ctx.response();
      response.write("route3");

      response.end();
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
