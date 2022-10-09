package com.example.web.forward;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.AllowForwardHeaders;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.LoggerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForwardVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(ForwardVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.route().handler(LoggerHandler.create());
    router.allowForward(AllowForwardHeaders.ALL);
    router.route("/").handler(ctx -> {
      log.info("headers: {}", ctx.request().headers());
      ctx.response().end(ctx.request().remoteAddress().host());
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
