package com.example.web.cookie;

import java.util.Set;
import java.util.stream.Collectors;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.Cookie;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(CookieVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.route().handler(ctx -> {
      Cookie myCookie = ctx.request().getCookie("otherCookie");
      Set<Cookie> cookies = ctx.request().cookies();
      log.info("myCookie: {}, cookies: {}", JsonObject.mapFrom(myCookie), cookies.stream().map(JsonObject::mapFrom).collect(Collectors.toList()));

      ctx.response().addCookie(Cookie.cookie("otherCookie", "somevalue"));
      ctx.end();
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
