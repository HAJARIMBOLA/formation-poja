package com.formation.hei.service.subscription;

import com.formation.hei.endpoint.rest.model.SubscriptionResponse;
import com.formation.hei.exception.AlreadySubscribedException;
import com.formation.hei.exception.CourseNotFoundException;
import com.formation.hei.exception.UserNotFoundException;
import com.formation.hei.mail.Email;
import com.formation.hei.mail.Mailer;
import com.formation.hei.model.Course;
import com.formation.hei.model.User;
import com.formation.hei.repository.CourseRepository;
import com.formation.hei.repository.UserRepository;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionService {

  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final Mailer mailer;

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
    sendConfirmationEmail(user, course);

    return new SubscriptionResponse(
        userId,
        courseId,
        "Inscription confirmée pour " + user.getFirstName() + " au cours " + course.getTitle());
  }

  private void sendConfirmationEmail(User user, Course course) {
    var recipient = toInternetAddress(user.getEmail());
    mailer.accept(
        new Email(
            recipient,
            List.of(),
            List.of(),
            "Confirmation d'inscription - " + course.getTitle(),
            buildConfirmationBody(user, course),
            List.of()));
  }

  private InternetAddress toInternetAddress(String email) {
    try {
      return new InternetAddress(email);
    } catch (AddressException e) {
      throw new IllegalArgumentException("L'adresse e-mail de l'utilisateur est invalide", e);
    }
  }

  private String buildConfirmationBody(User user, Course course) {
    return "<p>Bonjour "
        + user.getFirstName()
        + ",</p>"
        + "<p>Votre inscription au cours <b>"
        + course.getTitle()
        + "</b> a bien été enregistrée.</p>"
        + "<p>À très bientôt !</p>";
  }
}
