package com.tms.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

@Component
@Slf4j
public class GlobalExceptionHandler extends DataFetcherExceptionResolverAdapter {
    
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        log.error("GraphQL Error: ", ex);
        
        if (ex instanceof ResourceNotFoundException) {
            return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.NOT_FOUND)
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
        }
        
        if (ex instanceof BadCredentialsException) {
            return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.UNAUTHORIZED)
                .message("Invalid username or password")
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
        }
        
        if (ex instanceof AccessDeniedException) {
            return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.FORBIDDEN)
                .message("Access denied")
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
        }
        
        if (ex instanceof BindException) {
            return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message("Validation error: " + ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
        }
        
        return GraphqlErrorBuilder.newError()
            .errorType(ErrorType.INTERNAL_ERROR)
            .message("Internal server error: " + ex.getMessage())
            .path(env.getExecutionStepInfo().getPath())
            .location(env.getField().getSourceLocation())
            .build();
    }
}
