package com.example.web.subroute;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class SubRouteVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router restAPI = Router.router(vertx);
    restAPI.get("/products/:productID").handler(ctx -> ctx.end("get" + ctx.pathParam("productID")));
    restAPI.post("/products/:productID").handler(ctx -> ctx.end("post" + ctx.pathParam("productID")));
    restAPI.delete("/products/:productID").handler(ctx -> ctx.end("delete" + ctx.pathParam("productID")));

    Router mainRouter = Router.router(vertx);
    Route route = mainRouter.route("/productsAPI/*");
    route.handler(ctx -> {});
    route.subRouter(restAPI);
    // will throw: This Route is exclusive for already mounted sub router.
//    route.handler(ctx -> {});

    vertx.createHttpServer().requestHandler(mainRouter).listen(8080, res -> {
      if(res.succeeded()){
        startPromise.complete();
      }else{
        startPromise.fail(res.cause());
      }
    });
  }
}
