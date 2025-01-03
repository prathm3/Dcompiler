package com.service.compiler.CodeExecutionService.api.handler;

import com.service.compiler.CodeExecutionService.utils.ResponseUtils;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.validation.BadRequestException;
import io.vertx.ext.web.validation.BodyProcessorException;
import io.vertx.ext.web.validation.ParameterProcessorException;
import io.vertx.ext.web.validation.RequestPredicateException;

public class ErrorHandler {

    private ErrorHandler() {

    }

    public static void buildHandler(Router router) {
        router.errorHandler(400, rc -> {
            if (rc.failure() instanceof BadRequestException) {
                if (rc.failure() instanceof ParameterProcessorException) {
                    // Something went wrong while parsing/validating a parameter
                    ResponseUtils.buildErrorResponse(rc, new IllegalArgumentException("Path parameter is invalid"));
                } else if (rc.failure() instanceof BodyProcessorException | rc.failure() instanceof RequestPredicateException) {
                    // Something went wrong while parsing/validating the body
                    ResponseUtils.buildErrorResponse(rc, new IllegalArgumentException("Request body is invalid"));
                }
            }
        });
    }

}
