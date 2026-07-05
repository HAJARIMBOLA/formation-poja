package com.example.demo.service.enrollment.exception;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(Long courseId) {
        super("Course not found with id=" + courseId);
    }
}