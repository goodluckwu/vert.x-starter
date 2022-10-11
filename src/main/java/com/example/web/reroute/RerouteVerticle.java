package com.example.web.reroute;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RerouteVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(RerouteVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.get("/some/path").handler(ctx -> {
      ctx.put("foo", "bar");
      ctx.next();
    });

    router.get("/some/path/b").handler(ctx -> ctx.end(ctx.<String>get("foo")));

    router.get("/some/path").handler(ctx -> ctx.reroute("/some/path/b"));

    router.get("/my-pretty-notfound-handler").handler(ctx -> ctx.response().setStatusCode(4044).end("not found"));
    router.errorHandler(404, ctx -> {
      ctx.reroute("/my-pretty-notfound-handler");
    });

    vertx.createHttpServer().requestHandler(router).listen(8080, res -> {
      if(res.succeeded()){
        startPromise.complete();
      }else{
        startPromise.fail(res.cause());
      }
    });
  }
}
