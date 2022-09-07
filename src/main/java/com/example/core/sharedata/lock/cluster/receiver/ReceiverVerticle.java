package com.example.core.sharedata.lock.cluster.receiver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.shareddata.Lock;
import io.vertx.core.shareddata.SharedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiverVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(ReceiverVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    EventBus eventBus = vertx.eventBus();
    eventBus.consumer("news.uk.sport", message -> {
      log.info("I have received a message: {}", message.body());

      SharedData sharedData = vertx.sharedData();
      sharedData.getLock("mylock", res -> {
        if(res.succeeded()){
          // 获得锁
          Lock lock = res.result();
          log.info("ReceiverVerticle 获得集群锁 mylock");

          // 5秒后释放
          vertx.setTimer(5000, tid -> lock.release());
        }else {
          log.error("ReceiverVerticle 获取锁失败!", res.cause());
        }
      });

      sharedData.getLockWithTimeout("mylock2", 1000, res -> {
        if(res.succeeded()){
          // 获得锁
          Lock lock = res.result();
          log.info("ReceiverVerticle 获得集群锁 mylock2");

          // 5秒后释放
          vertx.setTimer(5000, tid -> lock.release());
        }else {
          log.error("ReceiverVerticle 获取锁失败!", res.cause());
        }
      });

      sharedData.getLocalLock("mylock3", res -> {
        if(res.succeeded()){
          // 获得锁
          Lock lock = res.result();
          log.info("ReceiverVerticle 获得本地锁 mylock3");

          // 5秒后释放
          vertx.setTimer(5000, tid -> lock.release());
        }else {
          log.error("ReceiverVerticle 获取锁失败!", res.cause());
        }
      });

      sharedData.getLocalLock("mylock3", res -> {
        if(res.succeeded()){
          // 获得锁
          Lock lock = res.result();
          log.info("ReceiverVerticle 获得本地锁 mylock3");

          // 5秒后释放
          vertx.setTimer(5000, tid -> lock.release());
        }else {
          log.error("ReceiverVerticle 获取锁失败!", res.cause());
        }
      });

      sharedData.getLocalLockWithTimeout("mylock4", 1000, res -> {
        if(res.succeeded()){
          // 获得锁
          Lock lock = res.result();
          log.info("ReceiverVerticle 获得本地锁 mylock4");

          // 5秒后释放
          vertx.setTimer(5000, tid -> lock.release());
        }else {
          log.error("ReceiverVerticle 获取锁失败!", res.cause());
        }
      });

      message.reply("how interesting!");
    }).completionHandler(res -> {
      if(res.succeeded()){
        log.info("The handler registration has reached all nodes");
        startPromise.complete();
        log.info("receiver started");
      }else {
        log.error("Registration failed!");
        startPromise.fail(res.cause());
      }
    });
  }
}
