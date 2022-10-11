package com.example.web.subroute;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new SubRouteVerticle(), dr -> {
      if(dr.failed()){
        log.error("failed to deploy SubRouteVerticle", dr.cause());
        vertx.close().onFailure(cr -> {
          log.error("failed to close vertx", cr.getCause());
        });
      }
    });
  }
}
