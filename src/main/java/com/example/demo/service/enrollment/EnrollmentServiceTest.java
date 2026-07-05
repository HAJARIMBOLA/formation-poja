package com.example.demo.service.enrollment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.domain.course.Course;
import com.example.demo.domain.course.CourseRepository;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import com.example.demo.endpoint.event.EventProducer;
import com.example.demo.endpoint.event.model.EnrollmentEvent;
import com.example.demo.service.enrollment.exception.AlreadyEnrolledException;
import com.example.demo.service.enrollment.exception.CourseNotFoundException;
import com.example.demo.service.enrollment.exception.UserNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnrollmentServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final CourseRepository courseRepository = mock(CourseRepository.class);
    @SuppressWarnings("unchecked")
    private final EventProducer<EnrollmentEvent> eventProducer = mock(EventProducer.class);

    private EnrollmentService enrollmentService;

    @BeforeEach
    void setUp() {
        enrollmentService = new EnrollmentService(userRepository, courseRepository, eventProducer);
    }

    @Test
    void enroll_addsTheCourseToTheUser_andPublishesAnEnrollmentEvent() {
        User user = new User(1L, "Ada Lovelace", "ada@example.com", new java.util.HashSet<>());
        Course course = new Course(2L, "Algorithmique avancée", new java.util.HashSet<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));

        enrollmentService.enroll(1L, 2L);

        assertThat(user.getCourses()).contains(course);
        verify(userRepository).save(user);
        verify(eventProducer).accept(any());
    }

    @Test
    void enroll_throws_whenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.enroll(1L, 2L))
                .isInstanceOf(UserNotFoundException.class);
        verify(eventProducer, never()).accept(any());
    }

    @Test
    void enroll_throws_whenCourseDoesNotExist() {
        User user = new User(1L, "Ada Lovelace", "ada@example.com", new java.util.HashSet<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.enroll(1L, 2L))
                .isInstanceOf(CourseNotFoundException.class);
        verify(eventProducer, never()).accept(any());
    }

    @Test
    void enroll_throws_whenUserAlreadyEnrolled() {
        Course course = new Course(2L, "Algorithmique avancée", new java.util.HashSet<>());
        User user =
                new User(1L, "Ada Lovelace", "ada@example.com", new java.util.HashSet<>(java.util.Set.of(course)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));

        assertThatThrownBy(() -> enrollmentService.enroll(1L, 2L))
                .isInstanceOf(AlreadyEnrolledException.class);
        verify(userRepository, never()).save(any());
        verify(eventProducer, never()).accept(any());
    }
}