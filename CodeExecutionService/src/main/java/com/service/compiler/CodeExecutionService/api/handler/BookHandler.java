package com.service.compiler.CodeExecutionService.api.handler;

import com.service.compiler.CodeExecutionService.api.model.CodeRequest;
import com.service.compiler.CodeExecutionService.api.model.CodeResponse;
import com.service.compiler.CodeExecutionService.api.model.DebugCodeRequest;
import com.service.compiler.CodeExecutionService.api.model.DebugCodeResponse;
import com.service.compiler.CodeExecutionService.api.service.BookService;
import com.service.compiler.CodeExecutionService.utils.ResponseUtils;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;

public class BookHandler {

    private static final String ID_PARAMETER = "id";
    private static final String PAGE_PARAMETER = "page";
    private static final String LIMIT_PARAMETER = "limit";

    private final BookService bookService;

    public BookHandler(BookService bookService) {
        this.bookService = bookService;
    }

    public Future<CodeResponse> compile(RoutingContext rc) {
        final CodeRequest book = rc.getBodyAsJson().mapTo(CodeRequest.class);

      return bookService.compile(book)
        .onSuccess(success -> ResponseUtils.buildOkResponse(rc, success))
        .onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable));
    }

  public Future<CodeResponse> run(RoutingContext rc) {
    final CodeRequest book = rc.getBodyAsJson().mapTo(CodeRequest.class);

    return bookService.run(book)
      .onSuccess(success -> ResponseUtils.buildOkResponse(rc, success))
      .onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable));
  }

  public Future<DebugCodeResponse> debug(RoutingContext rc) {
      System.out.println("Here in debug");
    final DebugCodeRequest book = rc.getBodyAsJson().mapTo(DebugCodeRequest.class);
    System.out.println(book);
    return bookService.debug(book)
      .onSuccess(success -> ResponseUtils.buildOkResponse(rc, success))
      .onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable));
  }

}
