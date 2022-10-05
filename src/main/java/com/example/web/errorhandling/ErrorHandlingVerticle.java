package com.example.web.errorhandling;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandlingVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(ErrorHandlingVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    int port = 8080;
    Router router = Router.router(vertx);
    router.get("/error/:num1/:num2").handler(LoggerHandler.create()).handler(ctx -> {
      String num1 = ctx.pathParam("num1");
      String num2 = ctx.pathParam("num2");
      ctx.response().end(String.format("%s / %s = %s", num1, num2, Integer.parseInt(num1) / Integer.parseInt(num2)));
    }).failureHandler(ctx -> {
      int statusCode = ctx.statusCode();
      log.error("statusCode: {}", statusCode);
      ctx.response().setStatusCode(statusCode).end("error");
    });

    vertx.createHttpServer().requestHandler(router).listen(port, res -> {
      if(res.succeeded()){
        log.info("http server started at listening port: {}", port);
        startPromise.complete();
      }else {
        startPromise.fail(res.cause());
      }
    });
  }
}
