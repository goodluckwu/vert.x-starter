package com.example.web.validation;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import static io.vertx.ext.web.validation.builder.Parameters.param;
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;
import static io.vertx.json.schema.common.dsl.Schemas.intSchema;
import io.vertx.json.schema.draft7.Draft7SchemaParser;
import static io.vertx.json.schema.draft7.dsl.Keywords.maximum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(ValidationVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.route("/valid")
      .handler(ValidationHandlerBuilder.create(Draft7SchemaParser.create(SchemaRouter.create(vertx, new SchemaRouterOptions()))).queryParameter(param("aParam", intSchema().with(maximum(100)))).build())
      .handler(ctx -> {
        RequestParameters params = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        ctx.response().end(params.queryParameter("aParam").getInteger().toString());
      });

    int port = 8080;
    vertx.createHttpServer().requestHandler(router).listen(port, res -> {
      if(res.succeeded()){
        log.info("Http server started on port: {}", port);
        startPromise.complete();
      }else {
        startPromise.fail(res.cause());
      }
    });
  }
}
