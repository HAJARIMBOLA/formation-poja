package com.formation.hei.data.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.formation.hei.data.entity.Course;
import com.formation.hei.data.entity.User;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class UserCourseRepositoryIT extends FacadeIT {

  @Autowired private UserRepository userRepository;

  @Autowired private CourseRepository courseRepository;

  @Test
  @Transactional
  void enregistre_et_relie_un_utilisateur_a_plusieurs_cours() {
    var javaCourse = new Course();
    javaCourse.setTitle("Java avancé");
    javaCourse.setStartDate(LocalDate.of(2026, 9, 1));
    javaCourse.setEndDate(LocalDate.of(2026, 12, 1));

    var springCourse = new Course();
    springCourse.setTitle("Spring Boot");
    springCourse.setStartDate(LocalDate.of(2027, 1, 10));
    springCourse.setEndDate(LocalDate.of(2027, 3, 10));

    courseRepository.saveAll(List.of(javaCourse, springCourse));

    var user = new User();
    user.setFirstName("Jean");
    user.setLastName("Dupont");
    user.setUserName("jdupont");
    user.setEmail("jean.dupont@example.com");

    user.enrollTo(javaCourse);
    user.enrollTo(springCourse);
    userRepository.save(user);

    var otherUser = new User();
    otherUser.setFirstName("Marie");
    otherUser.setLastName("Rakoto");
    otherUser.setUserName("mrakoto");
    otherUser.setEmail("marie.rakoto@example.com");
    otherUser.enrollTo(javaCourse);
    userRepository.save(otherUser);

    var savedJava = courseRepository.findById(javaCourse.getId()).orElseThrow();
    var savedUser = userRepository.findById(user.getId()).orElseThrow();

    // A course can contain several users.
    assertThat(savedJava.getUsers())
        .extracting(User::getUserName)
        .containsExactlyInAnyOrder("jdupont", "mrakoto");

    // A user can be enrolled in several courses.
    assertThat(savedUser.getCourses())
        .extracting(Course::getTitle)
        .containsExactlyInAnyOrder("Java avancé", "Spring Boot");

    // findByUserName / findByTitleContainingIgnoreCase work as expected.
    assertThat(userRepository.findByUserName("jdupont")).isPresent();
    assertThat(courseRepository.findByTitleContainingIgnoreCase("java")).hasSize(1);
  }
}
