package com.example.web.auth.basic;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BasicAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class AuthenticationVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

    BasicAuthHandler basicAuthHandler = BasicAuthHandler.create((credentials, resultHandler) -> {
      System.out.println("credentials: " + JsonObject.mapFrom(credentials));
      if("root".equals(credentials.getString("username")) && "root".equals(credentials.getString("password"))){
        resultHandler.handle(Future.succeededFuture(User.create(credentials)));
      }else{
        resultHandler.handle(Future.failedFuture("username or password error"));
      }
    });

    router.get("/authentication").handler(basicAuthHandler).handler(ctx -> {
      User user = ctx.user();
      System.out.println("user: " + user.principal());
      ctx.end(user.subject());
    });

    router.get("/logout").handler(ctx -> {
      ctx.clearUser();
      ctx.session().destroy();
      ctx.end();
    });

    vertx.createHttpServer().requestHandler(router).listen(8080, res ->{
      if(res.succeeded()){
        startPromise.complete();
      }else{
        startPromise.fail(res.cause());
      }
    });
  }
}
