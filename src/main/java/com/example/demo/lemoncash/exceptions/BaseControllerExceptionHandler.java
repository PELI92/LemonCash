package com.example.demo.lemoncash.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.LOCKED;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@RequiredArgsConstructor
public abstract class BaseControllerExceptionHandler {

    protected ResponseEntity<ApiError> handleErrorResponse(ApiException e, int httpStatus) {
        return handleErrorResponse(e, httpStatus, buildApiError(e, httpStatus));
    }

    protected ResponseEntity<ApiError> handleErrorResponse(Exception e, HttpStatus httpStatus) {
        return handleErrorResponse(e, httpStatus.value(), buildApiError(e, httpStatus));
    }

    protected ResponseEntity<ApiError> handleErrorResponse(Exception e, int httpStatus, ApiError apiError) {
        if (shouldIgnoreError(httpStatus)) {
            return ResponseEntity.ok(apiError);
        }

        if (httpStatus != LOCKED.value()) {
            System.out.println(e);
        }
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    protected ApiError buildApiError(ApiException e, int status) {
        return ApiError.builder()
                .error(e.getCode())
                .message(e.getDescription())
                .status(status)
                .build();
    }

    protected ApiError buildApiError(Exception e, HttpStatus httpStatus) {
        return ApiError.builder()
                .error(httpStatus.getReasonPhrase().toLowerCase().replace(" ", "_"))
                .message(e.getMessage())
                .status(httpStatus.value())
                .build();
    }

    protected boolean shouldIgnoreError(int httpStatus) {
        return !isRecoverableError(httpStatus);
    }

    protected boolean isRecoverableError(int httpStatus) {
        return httpStatus == LOCKED.value() ||
                httpStatus == TOO_MANY_REQUESTS.value() ||
                httpStatus >= INTERNAL_SERVER_ERROR.value();
    }

}
