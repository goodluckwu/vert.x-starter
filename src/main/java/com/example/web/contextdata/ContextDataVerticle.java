package com.example.web.contextdata;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextDataVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(ContextDataVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.get("/some/path/*").handler(ctx -> {
      ctx.put("foo", "bar");
      ctx.next();
    });

    router.get("/some/path/other").handler(ctx -> {
      String bar = ctx.get("foo");
      log.info("data:{}, foo: {}", ctx.data(), bar);
      ctx.response().end(bar);
    });

    int port = 8080;
    vertx.createHttpServer().requestHandler(router).listen(port, res -> {
      if(res.succeeded()){
        log.info("Http server started on port: {}", port);
        startPromise.complete();
      }else {
        startPromise.fail(res.cause());
      }
    });
  }
}
