package com.example.web.auth.basic;

import io.vertx.core.Vertx;

public class App {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new AuthenticationVerticle(), dr -> {
      if(dr.failed()){
        vertx.close().onFailure(System.err::println);
      }
    });
  }
}
