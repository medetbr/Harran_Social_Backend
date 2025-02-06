package com.edu.harran.social.exception.academic;

import com.edu.harran.social.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AcademicExceptionHandler {
    @ExceptionHandler(AcademicCreateException.class)
    public ResponseEntity<?> uniqueName(AcademicCreateException academicCreateException){
        return getErrorResponse(academicCreateException.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private ResponseEntity<ErrorResponse> getErrorResponse(String message, HttpStatus httpStatus) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
