package com.example.demo.endpoint.rest.controller.enrollment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.example.demo.conf.FacadeIT;
import com.example.demo.domain.course.Course;
import com.example.demo.domain.course.CourseRepository;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import com.example.demo.endpoint.event.EventProducer;
import com.example.demo.endpoint.event.model.EnrollmentEvent;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * The real EnrollmentEvent publication (EventProducer -> EventBridge -> SQS) and its asynchronous
 * consumption by EnrollmentEventService only happen once deployed on AWS (a separate Lambda
 * consumes the queue). Locally/in CI there is no such infrastructure, so EventProducer is mocked
 * here: this test verifies the enrollment itself and that an event *would* be published, not the
 * full round trip to the mailbox. EnrollmentEventServiceTest covers the consumer side in
 * isolation.
 */
class EnrollmentControllerIT extends FacadeIT {

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private UserRepository userRepository;
    @Autowired private CourseRepository courseRepository;

    @MockBean private EventProducer<EnrollmentEvent> eventProducer;

    private User user;
    private Course course;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("Ada Lovelace", "ada@example.com"));
        course = courseRepository.save(new Course("Algorithmique avancée"));
    }

    @Test
    void enroll_returns201_persistsRelationship_andPublishesEnrollmentEvent() {
        ResponseEntity<Void> response =
                restTemplate.postForEntity(
                        "/users/{userId}/courses/{courseId}", null, Void.class, user.getId(), course.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        User reloaded = userRepository.findById(user.getId()).orElseThrow();
        assertThat(reloaded.getCourses()).extracting(Course::getId).containsExactly(course.getId());

        verify(eventProducer).accept(any(Collection.class));
    }

    @Test
    void enroll_returns404_whenUserDoesNotExist() {
        ResponseEntity<Void> response =
                restTemplate.postForEntity(
                        "/users/{userId}/courses/{courseId}", null, Void.class, 999_999L, course.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void enroll_returns404_whenCourseDoesNotExist() {
        ResponseEntity<Void> response =
                restTemplate.postForEntity(
                        "/users/{userId}/courses/{courseId}", null, Void.class, user.getId(), 999_999L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void enroll_returns409_whenAlreadyEnrolled() {
        restTemplate.postForEntity(
                "/users/{userId}/courses/{courseId}", null, Void.class, user.getId(), course.getId());

        ResponseEntity<Void> response =
                restTemplate.postForEntity(
                        "/users/{userId}/courses/{courseId}", null, Void.class, user.getId(), course.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}