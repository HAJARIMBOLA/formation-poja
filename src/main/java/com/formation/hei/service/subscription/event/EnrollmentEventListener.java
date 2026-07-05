package com.formation.hei.service.subscription.event;

import com.formation.hei.mail.Email;
import com.formation.hei.mail.Mailer;
import com.formation.hei.model.Course;
import com.formation.hei.model.User;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Consomme les {@link EnrollmentEvent} pour envoyer l'e-mail de confirmation d'inscription. Le
 * traitement est asynchrone ({@link Async}) afin de ne pas ralentir la réponse HTTP du endpoint
 * d'inscription.
 */
@Slf4j
@Component
@AllArgsConstructor
public class EnrollmentEventListener {

  private final Mailer mailer;

  @Async
  @EventListener
  public void onEnrollment(EnrollmentEvent event) {
    var user = event.user();
    var course = event.course();

    var recipient = toInternetAddress(user.getEmail());
    mailer.accept(
        new Email(
            recipient,
            List.of(),
            List.of(),
            "Confirmation d'inscription - " + course.getTitle(),
            buildConfirmationBody(user, course),
            List.of()));

    log.info(
        "E-mail de confirmation envoyé à {} pour le cours {}", user.getEmail(), course.getId());
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
