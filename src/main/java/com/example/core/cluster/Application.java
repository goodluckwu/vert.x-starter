package com.example.core.cluster;

import com.hazelcast.config.Config;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.hazelcast.ConfigUtil;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    Config config = ConfigUtil.loadConfig();
    config.setClusterName("my-cluster-name");

    HazelcastClusterManager hazelcastClusterManager = new HazelcastClusterManager(config);

    Vertx.clusteredVertx(new VertxOptions().setClusterManager(hazelcastClusterManager), res -> {
      if(res.succeeded()){
        Vertx vertx = res.result();
      }else{
        log.error("cluster failed!", res.cause());
      }
    });
  }
}
