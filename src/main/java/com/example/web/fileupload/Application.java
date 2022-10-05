package com.example.web.fileupload;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new FileUploadVerticle(), res -> {
      if(res.succeeded()){
        log.info("Deployed successfully, deploymentId: {}", res.result());
      } else {
        log.error("failed to deploy FileUploadVerticle", res.cause());
      }
    });
  }
}
