package com.ssafy.global.response.handler;

import com.ssafy.global.response.code.ErrorCode;
import com.ssafy.global.response.code.ResponseCode;
import com.ssafy.global.response.exception.RestApiException;
import com.ssafy.global.response.structure.ErrorResponse;


import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * RestApiException
     */
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleRestApiException(final RestApiException e) {
        logger.debug("Handled API exception: {}", e.getResponseCode());

        final ResponseCode responseCode = e.getResponseCode();
        return handleExceptionInternal(responseCode);
    }

    /**
     * IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(final IllegalArgumentException e) {
        logger.debug("Invalid request: {}", e.getMessage());

        final ResponseCode responseCode = ErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(responseCode, responseCode.getMessage());
    }

    /**
     * MethodArgumentNotValidException
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        logger.debug("Request body validation failed");

        final ResponseCode responseCode = ErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(ex, responseCode);
    }

    /**
     * ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException e) {
        logger.debug("Request parameter validation failed");

        final ResponseCode responseCode = ErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(e, responseCode);
    }

    /**
     * MethodArgumentTypeMismatchException
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        logger.debug("Request parameter type mismatch: {}", e.getName());

        final ResponseCode responseCode = ErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(responseCode, responseCode.getMessage());
    }

    /**
     *   handleExceptionInternal
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(final Exception e) {
        logger.error("Exception caught: ", e);

        final ResponseCode responseCode = ErrorCode.INTERNAL_SERVER_ERROR;

        return handleExceptionInternal(responseCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(final ResponseCode responseCode) {
        return ResponseEntity.status(responseCode.getHttpStatus())
                .body(ErrorResponse.of(responseCode));
    }

    private ResponseEntity<Object> handleExceptionInternal(final ResponseCode responseCode, final String message) {
        return ResponseEntity.status(responseCode.getHttpStatus())
                .body(ErrorResponse.of(responseCode, message));
    }

    private ResponseEntity<Object> handleExceptionInternal(final BindException e, final ResponseCode responseCode) {
        return ResponseEntity.status(responseCode.getHttpStatus())
                .body(ErrorResponse.of(e, responseCode));
    }

    private ResponseEntity<Object> handleExceptionInternal(final ConstraintViolationException e, final ResponseCode responseCode) {
        return ResponseEntity.status(responseCode.getHttpStatus())
                .body(ErrorResponse.of(e, responseCode));
    }

}
