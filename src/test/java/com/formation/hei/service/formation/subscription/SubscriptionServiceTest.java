package com.formation.hei.service.formation.subscription;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.formation.hei.exception.AlreadySubscribedException;
import com.formation.hei.exception.CourseNotFoundException;
import com.formation.hei.exception.UserNotFoundException;
import com.formation.hei.mail.Email;
import com.formation.hei.mail.Mailer;
import com.formation.hei.model.Course;
import com.formation.hei.model.User;
import com.formation.hei.repository.CourseRepository;
import com.formation.hei.repository.UserRepository;
import com.formation.hei.service.subscription.SubscriptionService;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private CourseRepository courseRepository;

  @Mock private Mailer mailer;

  private SubscriptionService subscriptionService;

  private User user;
  private Course course;

  @BeforeEach
  void setUp() {
    subscriptionService = new SubscriptionService(userRepository, courseRepository, mailer);
    user = new User(UUID.randomUUID(), "Jean", "Rakoto", "jrakoto", "jean.rakoto@example.com");
    course =
        new Course(UUID.randomUUID(), "Formation Spring Boot Avancé", Instant.now(), Instant.now());
  }

  @Test
  void inscrit_un_utilisateur_et_envoie_un_email_de_confirmation() {
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

    var response = subscriptionService.subscribe(user.getId(), course.getId());

    assertThat(response.userId()).isEqualTo(user.getId());
    assertThat(response.courseId()).isEqualTo(course.getId());
    assertThat(response.message()).contains("Jean").contains("Formation Spring Boot Avancé");
    assertThat(user.getCourseIds()).contains(course.getId());
    verify(userRepository).save(user);
    verify(mailer).accept(any(Email.class));
  }

  @Test
  void rejette_si_l_utilisateur_n_existe_pas() {
    when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> subscriptionService.subscribe(user.getId(), course.getId()))
        .isInstanceOf(UserNotFoundException.class);

    verifyNoInteractions(mailer);
  }

  @Test
  void rejette_si_le_cours_n_existe_pas() {
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(courseRepository.findById(course.getId())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> subscriptionService.subscribe(user.getId(), course.getId()))
        .isInstanceOf(CourseNotFoundException.class);

    verifyNoInteractions(mailer);
  }

  @Test
  void rejette_si_deja_inscrit() {
    user.getCourseIds().add(course.getId());
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

    assertThatThrownBy(() -> subscriptionService.subscribe(user.getId(), course.getId()))
        .isInstanceOf(AlreadySubscribedException.class);

    verifyNoInteractions(mailer);
  }
}
