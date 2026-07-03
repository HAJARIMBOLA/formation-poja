package com.formation.hei.endpoint.rest.controller;

import com.formation.hei.exception.AlreadySubscribedException;
import com.formation.hei.exception.CourseNotFoundException;
import com.formation.hei.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler({UserNotFoundException.class, CourseNotFoundException.class})
  public ResponseEntity<String> handleNotFound(RuntimeException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler(AlreadySubscribedException.class)
  public ResponseEntity<String> handleAlreadySubscribed(AlreadySubscribedException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  }
}