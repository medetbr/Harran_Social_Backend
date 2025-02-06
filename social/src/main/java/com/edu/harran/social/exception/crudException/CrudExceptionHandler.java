package com.edu.harran.social.exception.crudException;

import com.edu.harran.social.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@RestControllerAdvice
public class CrudExceptionHandler {
    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<?> uniqueName(InvalidIdException invalidIdException) {
        return getErrorResponse(invalidIdException.getMessage(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ErrorResponse> getErrorResponse(String message, HttpStatus httpStatus) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
