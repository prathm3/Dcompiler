package com.service.compiler.CodeExecutionService.api.router;

import com.service.compiler.CodeExecutionService.api.handler.BookHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;

public class BookRouter {

    private final Vertx vertx;
    private final BookHandler bookHandler;
    private Router bookRouter;

    public BookRouter(Vertx vertx,
                      BookHandler bookHandler) {
        this.vertx = vertx;
        this.bookHandler = bookHandler;
        this.bookRouter = null;
    }

    public void setRouter(Router router) {
      this.bookRouter = router;
        router.mountSubRouter("/api/v1", buildBookRouter());
    }

    private Router buildBookRouter() {
        bookRouter.route().handler(BodyHandler.create());
        bookRouter.post("/compile").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(bookHandler::compile);
        bookRouter.post("/run").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(bookHandler::run);
        bookRouter.post("/debug").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(bookHandler::debug);

        return bookRouter;
    }

}
