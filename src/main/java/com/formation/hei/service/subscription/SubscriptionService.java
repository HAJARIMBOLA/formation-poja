package com.formation.hei.service.subscription;

import com.formation.hei.endpoint.rest.model.SubscriptionResponse;
import com.formation.hei.exception.AlreadySubscribedException;
import com.formation.hei.exception.CourseNotFoundException;
import com.formation.hei.exception.UserNotFoundException;
import com.formation.hei.repository.CourseRepository;
import com.formation.hei.repository.UserRepository;
import com.formation.hei.service.subscription.event.EnrollmentEvent;
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
