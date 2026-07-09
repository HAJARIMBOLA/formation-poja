package com.example.demo.service.subscription.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import com.example.demo.model.Course;
import com.example.demo.model.User;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EnrollmentEventListenerTest {

  @Mock private Mailer mailer;

  private EnrollmentEventListener enrollmentEventListener;

  @BeforeEach
  void setUp() {
    enrollmentEventListener = new EnrollmentEventListener(mailer);
  }

  @Test
  void envoie_un_email_de_confirmation_a_la_reception_de_l_evenement() {
    var user = new User(UUID.randomUUID(), "Jean", "Rakoto", "jrakoto", "jean.rakoto@example.com");
    var course =
        new Course(UUID.randomUUID(), "Formation Spring Boot Avancé", Instant.now(), Instant.now());

    enrollmentEventListener.onEnrollment(new EnrollmentEvent(user, course));

    var emailCaptor = ArgumentCaptor.forClass(Email.class);
    verify(mailer).accept(emailCaptor.capture());
    var email = emailCaptor.getValue();

    assertThat(email.to().getAddress()).isEqualTo(user.getEmail());
    assertThat(email.subject()).isEqualTo("Confirmation d'inscription - " + course.getTitle());
    assertThat(email.htmlBody()).contains(user.getFirstName()).contains(course.getTitle());
  }

  @Test
  void rejette_une_adresse_email_invalide() {
    var user = new User(UUID.randomUUID(), "Jean", "Rakoto", "jrakoto", "not an email");
    var course =
        new Course(UUID.randomUUID(), "Formation Spring Boot Avancé", Instant.now(), Instant.now());

    assertThatThrownBy(
            () -> enrollmentEventListener.onEnrollment(new EnrollmentEvent(user, course)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("L'adresse e-mail de l'utilisateur est invalide");
  }
}
