package com.example.web.auth.redirect;

import java.util.Optional;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.FormLoginHandler;
import io.vertx.ext.web.handler.RedirectAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class RedirectHandlerVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.route().handler(StaticHandler.create());
    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

    AuthenticationProvider authenticationProvider = (credentials, resultHandler) -> {
      System.out.println("credentials: " + JsonObject.mapFrom(credentials));
      if("root".equals(credentials.getString("username")) && "root".equals(credentials.getString("password"))){
        resultHandler.handle(Future.succeededFuture(User.create(credentials)));
      }else{
        resultHandler.handle(Future.failedFuture("username or password error"));
      }
    };

    router.route("/loginpage").handler(ctx -> {
      ctx.response().sendFile("webroot/loginpage.html");
    });

    router.post("/login").handler(FormLoginHandler.create(authenticationProvider));

    router.get("/logout").handler(ctx -> {
      ctx.clearUser();
      ctx.redirect("/loginpage");
    });

    router.route("/authentication").handler(RedirectAuthHandler.create(authenticationProvider))
      .handler(ctx  -> {
        System.out.println("user: " + Optional.ofNullable(ctx.user()).map(User::principal).orElse(new JsonObject()));
        ctx.response().sendFile("webroot/index.html");
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
