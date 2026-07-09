package com.example.demo.service.subscription;

import com.example.demo.endpoint.rest.model.SubscriptionResponse;
import com.example.demo.exception.AlreadySubscribedException;
import com.example.demo.exception.CourseNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.subscription.event.EnrollmentEvent;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionService {

  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final ApplicationEventPublisher eventPublisher;

  public SubscriptionResponse subscribe(UUID userId, UUID courseId) {
    var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    var course =
        courseRepository
            .findById(courseId)
            .orElseThrow(() -> new CourseNotFoundException(courseId));

    boolean subscribed = user.getCourseIds().add(courseId);
    if (!subscribed) {
      throw new AlreadySubscribedException(userId, courseId);
    }

    userRepository.save(user);
    eventPublisher.publishEvent(new EnrollmentEvent(user, course));

    return new SubscriptionResponse(
        userId,
        courseId,
        "Inscription confirmée pour " + user.getFirstName() + " au cours " + course.getTitle());
  }
}
