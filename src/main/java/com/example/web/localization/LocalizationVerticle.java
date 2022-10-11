package com.example.web.localization;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.LanguageHeader;
import io.vertx.ext.web.Router;

public class LocalizationVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.route("/localized").handler(ctx -> {
      for(LanguageHeader language : ctx.acceptableLanguages()){
        switch (language.tag()){
          case "en":
            ctx.end("Hello!");
            return;
          case "zh":
            ctx.response().putHeader("Content-Type", "text/html;charset=utf-8");
            ctx.end("你好！");
              return;
        }
      }
      ctx.end("Sorry we don't speak: " + ctx.preferredLanguage());
    });

    vertx.createHttpServer().requestHandler(router).listen(8080, res -> {
      if(res.succeeded()) {
        startPromise.complete();
      }else {
        startPromise.fail(res.cause());
      }
    });
  }
}
