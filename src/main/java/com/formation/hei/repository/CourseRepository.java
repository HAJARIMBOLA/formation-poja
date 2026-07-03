package com.formation.hei.repository;

import com.formation.hei.model.Course;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository {
  Optional<Course> findById(UUID id);
}
