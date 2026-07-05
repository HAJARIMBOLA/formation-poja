package com.example.demo.endpoint.event.model;

import java.time.Duration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Event published once a user has successfully been enrolled in a course. It is consumed
 * asynchronously (see com.example.demo.service.event.EnrollmentEventService) to send the
 * confirmation e-mail, decoupling the enrollment transaction from the mail sending.
 *
 * <p>Getters AND setters are required (not just a constructor) because this object is
 * marshalled/unmarshalled to and from JSON by Jackson when it transits through EventBridge/SQS
 * (see EventProducer and ConsumableEventTyper).
 */
@Getter
@Setter
@NoArgsConstructor
public class EnrollmentEvent extends PojaEvent {

    private Long userId;
    private String userEmail;
    private String userName;
    private Long courseId;
    private String courseTitle;

    public EnrollmentEvent(
            Long userId, String userEmail, String userName, Long courseId, String courseTitle) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
    }

    @Override
    public Duration maxConsumerDuration() {
        return Duration.ofSeconds(30);
    }

    @Override
    public Duration maxConsumerBackoffBetweenRetries() {
        return Duration.ofSeconds(10);
    }
}