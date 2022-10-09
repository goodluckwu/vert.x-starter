package com.example.web.metadata;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetadataVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(MetadataVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.route("/metadata")
      .putMetadata("metadata-key", "123")
      .handler(ctx -> {
        Route route = ctx.currentRoute();
        String value = route.getMetadata("metadata-key");
        ctx.end(value);
      });

    vertx.createHttpServer().requestHandler(router).listen(8080, res -> {
      if(res.succeeded()){
        log.info("http server started on port: 8080");
        startPromise.complete();
      }else {
        startPromise.fail(res.cause());
      }
    });
  }
}
