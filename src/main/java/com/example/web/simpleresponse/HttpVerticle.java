package com.example.web.simpleresponse;

import java.util.zip.CRC32;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
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

    router.get("/simple/json").respond(ctx -> Future.succeededFuture(new JsonObject().put("hello", "world")));
    router.get("/simple/pojo").respond(ctx -> Future.succeededFuture(new Pojo("Hello World!")));
    router.get("/simple/text").respond(ctx -> ctx.response().putHeader("Content-Type", "text/plain").end("Hello World"));
    router.get("/simple/chunk").respond(ctx -> ctx.response().setChunked(true).write("Write some text..."));

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

  static class Pojo {

    public Pojo(String name) {
      this.name = name;
    }

    private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return "Pojo{" +
        "name='" + name + '\'' +
        '}';
    }
  }
}
