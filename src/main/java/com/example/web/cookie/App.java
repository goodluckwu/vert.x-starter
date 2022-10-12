package com.example.web.cookie;

import io.vertx.core.Vertx;

public class App {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new CookieVerticle(), dr -> {
      if(dr.failed()){
        vertx.close().onFailure(System.err::println);
      }
    });
  }
}
