package com.example.demo.lemoncash.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

import java.sql.SQLException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Default Controller Exception Handler.
 * To override behaviour for specific Exceptions, declare a new @ControllerAdvice with
 * annotation @Order with a lower value of precedence.
 */
@ControllerAdvice
public class ControllerExceptionHandler extends BaseControllerExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> noHandlerFoundException(HttpServletRequest req, NoHandlerFoundException ex) {
        ApiError apiError = new ApiError("route_not_found", String.format("Route %s not found", req.getRequestURI()), NOT_FOUND.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnknownException(Exception e) {
        return handleErrorResponse(e, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(ApiException e) {
        return handleErrorResponse(e, e.getStatusCode());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException e) {
        return handleErrorResponse(e, BAD_REQUEST);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiError> handleSQLException(ValidationException e) {
        return handleErrorResponse(e, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({HttpMessageConversionException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ApiError> handleJsonDeserializationException() {
        ApiError apiError = ApiError.builder()
                .error("invalid_json_body")
                .message("Invalid json body")
                .status(BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingRequestParameterException(MissingServletRequestParameterException ex) {
        ApiError apiError = ApiError.builder()
                .error("missing_parameter")
                .message(String.format("Missing required parameter '%s'", ex.getParameterName()))
                .status(BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleInvalidRequestParameterTypeException(MethodArgumentTypeMismatchException ex) {
        ApiError apiError = ApiError.builder()
                .error("invalid_parameter_type")
                .message(String.format("Invalid value type '%s' for parameter '%s'", ex.getValue(), ex.getName()))
                .status(BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handlerEntityNotFoundException(EntityNotFoundException ex) {
        return handleErrorResponse(ex, NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handlerIllegalArgumentException(IllegalArgumentException ex) {
        return handleErrorResponse(ex, BAD_REQUEST);
    }
}
