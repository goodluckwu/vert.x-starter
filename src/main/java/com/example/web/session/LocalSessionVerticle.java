package com.example.web.session;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class LocalSessionVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)).setCookieless(true));

    router.get("/session").handler(ctx -> {
      Session session = ctx.session();
      System.out.printf("%s%n", session.data());
      session.put("foo", "bar");
      ctx.end(ctx.session().value());
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
