package com.service.compiler.CodeExecutionService;

import com.service.compiler.CodeExecutionService.utils.ApplicationUtils;
import com.service.compiler.CodeExecutionService.utils.LogUtils;
import com.service.compiler.CodeExecutionService.verticles.ApiVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start() {
    final long start = System.currentTimeMillis();

    deployApiVerticle(vertx)
      .onSuccess(success -> LOGGER.info(LogUtils.RUN_APP_SUCCESSFULLY_MESSAGE.buildMessage(System.currentTimeMillis() - start)))
      .onFailure(throwable -> LOGGER.error(throwable.getMessage()));
  }

  private Future<String> deployApiVerticle(Vertx vertx) {
    return vertx.deployVerticle(ApiVerticle.class.getName(),
      new DeploymentOptions().setInstances(ApplicationUtils.numberOfAvailableCores()));

  }

}
