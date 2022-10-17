package com.example.web.auth.redirect;

import io.vertx.core.Vertx;

public class App {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new RedirectHandlerVerticle()).onFailure(cause -> {
      cause.printStackTrace();
      vertx.close().onFailure(System.err::println);
    });
  }
}
