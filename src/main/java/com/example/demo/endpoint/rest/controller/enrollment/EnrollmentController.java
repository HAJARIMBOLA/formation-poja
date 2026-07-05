package com.example.demo.endpoint.rest.controller.enrollment;

import com.example.demo.service.enrollment.EnrollmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/users/{userId}/courses/{courseId}")
    public ResponseEntity<Void> enroll(
            @PathVariable Long userId, @PathVariable Long courseId) {
        enrollmentService.enroll(userId, courseId);
        return ResponseEntity.status(201).build();
    }
}