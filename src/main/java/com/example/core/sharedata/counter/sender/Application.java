package com.example.core.sharedata.counter.sender;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions(), res -> {
      if(res.succeeded()){
        Vertx vertx = res.result();
        vertx.deployVerticle(SenderVerticle.class, new DeploymentOptions().setInstances(1), deployRes -> {
          if(deployRes.succeeded()){
            log.info("Deployment id is: {}", deployRes.result());
          }else {
            log.error("Deployment failed!", deployRes.cause());
            throw new IllegalStateException(deployRes.cause());
          }
        });
      }else{
        log.error("cluster startup failed!", res.cause());
      }
    });
  }
}
