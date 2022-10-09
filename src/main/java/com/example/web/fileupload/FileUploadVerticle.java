package com.example.web.fileupload;

import java.util.List;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUploadVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(FileUploadVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.route().handler(LoggerHandler.create()).handler(BodyHandler.create());

    router.post("/uploads").handler(ctx -> {
      List<FileUpload> fileUploads = ctx.fileUploads();
      log.info("fileUploads.size(): {}", fileUploads.size());
      for (FileUpload fileUpload : fileUploads) {
        log.info("{}", fileUpload.uploadedFileName());
        log.info("{}", fileUpload.fileName());
        log.info("{}", fileUpload.name());
        log.info("{}", fileUpload.contentType());
        log.info("{}", fileUpload.contentTransferEncoding());
        log.info("{}", fileUpload.charSet());
      }
      System.out.println(ctx.request().formAttributes());
      System.out.println(ctx.body().asString());
      ctx.response().end(fileUploads.toString());
    });

    int port = 8080;
    vertx.createHttpServer().requestHandler(router).listen(port, res -> {
      if(res.succeeded()){
        log.info("HTTP server started on port: {}", port);
        startPromise.complete();
      } else {
        log.error("HTTP server failed to start", res.cause());
      }
    });
  }
}
