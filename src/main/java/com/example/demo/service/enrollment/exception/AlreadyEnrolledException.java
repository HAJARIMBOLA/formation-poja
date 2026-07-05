package com.example.demo.service.enrollment.exception;

public class AlreadyEnrolledException extends RuntimeException {

    public AlreadyEnrolledException(Long userId, Long courseId) {
        super("User id=" + userId + " is already enrolled in course id=" + courseId);
    }
}