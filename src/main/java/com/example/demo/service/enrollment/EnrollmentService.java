package com.example.demo.service.enrollment;

import com.example.demo.domain.course.Course;
import com.example.demo.domain.course.CourseRepository;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import com.example.demo.endpoint.event.EventProducer;
import com.example.demo.endpoint.event.model.EnrollmentEvent;
import com.example.demo.service.enrollment.exception.AlreadyEnrolledException;
import com.example.demo.service.enrollment.exception.CourseNotFoundException;
import com.example.demo.service.enrollment.exception.UserNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class EnrollmentService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EventProducer<EnrollmentEvent> eventProducer;

    @Transactional
    public void enroll(Long userId, Long courseId) {
        User user =
                userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Course course =
                courseRepository
                        .findById(courseId)
                        .orElseThrow(() -> new CourseNotFoundException(courseId));

        if (user.isEnrolledIn(course)) {
            throw new AlreadyEnrolledException(userId, courseId);
        }

        user.getCourses().add(course);
        userRepository.save(user);

        eventProducer.accept(
                List.of(
                        new EnrollmentEvent(
                                user.getId(), user.getEmail(), user.getName(), course.getId(), course.getTitle())));
    }
}