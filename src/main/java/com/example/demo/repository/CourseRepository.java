package com.example.demo.repository;

import com.example.demo.model.Course;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository {
  Optional<Course> findById(UUID id);
}
