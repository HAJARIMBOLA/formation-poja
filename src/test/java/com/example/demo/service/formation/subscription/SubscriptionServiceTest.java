package com.example.demo.service.formation.subscription;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.demo.exception.AlreadySubscribedException;
import com.example.demo.exception.CourseNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Course;
import com.example.demo.model.User;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.subscription.SubscriptionService;
import com.example.demo.service.subscription.event.EnrollmentEvent;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private CourseRepository courseRepository;

  @Mock private ApplicationEventPublisher eventPublisher;

  private SubscriptionService subscriptionService;

  private User user;
  private Course course;

  @BeforeEach
  void setUp() {
    subscriptionService = new SubscriptionService(userRepository, courseRepository, eventPublisher);
    user = new User(UUID.randomUUID(), "Jean", "Rakoto", "jrakoto", "jean.rakoto@example.com");
    course =
        new Course(UUID.randomUUID(), "Formation Spring Boot Avancé", Instant.now(), Instant.now());
  }

  @Test
  void inscrit_un_utilisateur_et_publie_un_evenement_d_inscription() {
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

    var response = subscriptionService.subscribe(user.getId(), course.getId());

    assertThat(response.userId()).isEqualTo(user.getId());
    assertThat(response.courseId()).isEqualTo(course.getId());
    assertThat(response.message()).contains("Jean").contains("Formation Spring Boot Avancé");
    assertThat(user.getCourseIds()).contains(course.getId());
    verify(userRepository).save(user);

    var eventCaptor = ArgumentCaptor.forClass(EnrollmentEvent.class);
    verify(eventPublisher).publishEvent(eventCaptor.capture());
    assertThat(eventCaptor.getValue().user()).isEqualTo(user);
    assertThat(eventCaptor.getValue().course()).isEqualTo(course);
  }

  @Test
  void rejette_si_l_utilisateur_n_existe_pas() {
    when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> subscriptionService.subscribe(user.getId(), course.getId()))
        .isInstanceOf(UserNotFoundException.class);

    verifyNoInteractions(eventPublisher);
  }

  @Test
  void rejette_si_le_cours_n_existe_pas() {
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(courseRepository.findById(course.getId())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> subscriptionService.subscribe(user.getId(), course.getId()))
        .isInstanceOf(CourseNotFoundException.class);

    verifyNoInteractions(eventPublisher);
  }

  @Test
  void rejette_si_deja_inscrit() {
    user.getCourseIds().add(course.getId());
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

    assertThatThrownBy(() -> subscriptionService.subscribe(user.getId(), course.getId()))
        .isInstanceOf(AlreadySubscribedException.class);

    verifyNoInteractions(eventPublisher);
  }
}
