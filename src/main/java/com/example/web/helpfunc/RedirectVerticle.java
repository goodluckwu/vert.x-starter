package com.example.web.helpfunc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

public class RedirectVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.route("/redirect").handler(ctx -> {
//      ctx.redirect("back");
      ctx.redirect("https://www.baidu.com");
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
