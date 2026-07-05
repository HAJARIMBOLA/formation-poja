package com.formation.hei.endpoint.rest.controller.subscription;

import com.formation.hei.endpoint.rest.model.SubscriptionResponse;
import com.formation.hei.service.subscription.SubscriptionService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SubscriptionController {

  private final SubscriptionService subscriptionService;

  @PostMapping("/users/{userId}/courses/{courseId}")
  public ResponseEntity<SubscriptionResponse> subscribe(
      @PathVariable UUID userId, @PathVariable UUID courseId) {
    var response = subscriptionService.subscribe(userId, courseId);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
