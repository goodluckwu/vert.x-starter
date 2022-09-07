package com.example.core.sharedata.lock.sender;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.shareddata.Lock;
import io.vertx.core.shareddata.SharedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SenderVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(SenderVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    int port = 8888;
    vertx.createHttpServer().requestHandler(req -> {
      SharedData sharedData = vertx.sharedData();

      sharedData.getLock("mylock", res -> {
        if(res.succeeded()){
          // 获得锁
          Lock lock = res.result();
          log.info("SenderVerticle 获得集群锁 mylock");

          // 5秒后释放
          vertx.setTimer(5000, tid -> lock.release());
        }else {
          log.error("SenderVerticle 获取锁失败!", res.cause());
        }
      });

      sharedData.getLockWithTimeout("mylock4", 1000, res -> {
        if(res.succeeded()){
          // 获得锁
          Lock lock = res.result();
          log.info("SenderVerticle 获得集群锁 mylock4");

          // 5秒后释放
          vertx.setTimer(5000, tid -> lock.release());
        }else {
          log.error("SenderVerticle 获取锁失败!", res.cause());
        }
      });

      sharedData.getLocalLock("mylock3", res -> {
        if(res.succeeded()){
          // 获得锁
          Lock lock = res.result();
          log.info("SenderVerticle 获得本地锁 mylock3");

          // 5秒后释放
          vertx.setTimer(5000, tid -> lock.release());
        }else {
          log.error("SenderVerticle 获取锁失败!", res.cause());
        }
      });

      sharedData.getLocalLock("mylock3", res -> {
        if(res.succeeded()){
          // 获得锁
          Lock lock = res.result();
          log.info("SenderVerticle 获得本地锁 mylock3");

          // 5秒后释放
          vertx.setTimer(5000, tid -> lock.release());
        }else {
          log.error("SenderVerticle 获取锁失败!", res.cause());
        }
      });

      sharedData.getLocalLockWithTimeout("mylock2", 1000, res -> {
        if(res.succeeded()){
          // 获得锁
          Lock lock = res.result();
          log.info("SenderVerticle 获得本地锁 mylock2");

          // 5秒后释放
          vertx.setTimer(5000, tid -> lock.release());
        }else {
          log.error("SenderVerticle 获取锁失败!", res.cause());
        }
      });

      EventBus eventBus = vertx.eventBus();
      eventBus.request("news.uk.sport", "Yay! Someone kicked a ball", reply -> {
        if(reply.succeeded()){
          log.info("reply: {}", reply.result().body());
        }else{
          log.error("reply失败！", reply.cause());
        }
      });
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Sender had send the message!");
    }).listen(port, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        log.info("HTTP server started on port {}", port);
      } else {
        log.error("HTTP server start failed", http.cause());
        startPromise.fail(http.cause());
      }
    });
  }
}
