package com.formation.hei.data.repository;

import com.formation.hei.data.entity.Course;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    List<Course> findByTitleContainingIgnoreCase(String title);
}