package com.example.demo.endpoint.rest.controller.enrollment;

import com.example.demo.endpoint.rest.dto.ApiError;
import com.example.demo.service.enrollment.exception.AlreadyEnrolledException;
import com.example.demo.service.enrollment.exception.CourseNotFoundException;
import com.example.demo.service.enrollment.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EnrollmentExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, CourseNotFoundException.class})
    public ResponseEntity<ApiError> handleNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(e.getMessage()));
    }

    @ExceptionHandler(AlreadyEnrolledException.class)
    public ResponseEntity<ApiError> handleConflict(AlreadyEnrolledException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(e.getMessage()));
    }
}