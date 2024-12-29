package com.service.compiler.CodeExecutionService.verticles;

import com.service.compiler.CodeExecutionService.api.handler.BookHandler;
import com.service.compiler.CodeExecutionService.api.handler.ErrorHandler;
import com.service.compiler.CodeExecutionService.api.router.BookRouter;
import com.service.compiler.CodeExecutionService.api.service.BookService;
import com.service.compiler.CodeExecutionService.utils.ConfigUtils;
import com.service.compiler.CodeExecutionService.utils.LogUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;

public class ApiVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiVerticle.class);

  @Override
  public void start(Promise<Void> promise) {
//
    final BookService bookService = new BookService();
    final BookHandler bookHandler = new BookHandler(bookService);
    final BookRouter bookRouter = new BookRouter(vertx, bookHandler);

    final Router router = Router.router(vertx);
    ErrorHandler.buildHandler(router);
    bookRouter.setRouter(router);

    buildHttpServer(vertx, promise, router);
  }

  private void buildHttpServer(Vertx vertx,
                               Promise<Void> promise,
                               Router router) {
//    int port = ConfigUtils.getInstance().getApplicationUtils().getServerPort();
//    System.out.println(port);
    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8888, http -> {
        if (http.succeeded()) {
          promise.complete();
          LOGGER.info(LogUtils.RUN_HTTP_SERVER_SUCCESS_MESSAGE.buildMessage(8888));
        } else {
          promise.fail(http.cause());
          LOGGER.info(LogUtils.RUN_HTTP_SERVER_ERROR_MESSAGE.buildMessage());
        }
      });
  }

}
