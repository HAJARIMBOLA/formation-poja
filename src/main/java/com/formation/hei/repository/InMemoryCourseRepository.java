package com.formation.hei.repository;

import com.formation.hei.model.Course;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryCourseRepository implements CourseRepository {

    private final Map<UUID, Course> courses = new ConcurrentHashMap<>();

    @Override
    public Optional<Course> findById(UUID id) {
        return Optional.ofNullable(courses.get(id));
    }

    @PostConstruct
    void seed() {
        var now = Instant.now();
        var demoCourse =
                new Course(
                        UUID.fromString("22222222-2222-2222-2222-222222222222"),
                        "Formation Spring Boot Avancé",
                        now.plus(7, ChronoUnit.DAYS),
                        now.plus(30, ChronoUnit.DAYS));
        courses.put(demoCourse.getId(), demoCourse);
    }
}