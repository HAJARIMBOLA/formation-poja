package com.example.demo.data.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void enrollTo_ajoute_le_cours_des_deux_cotes_de_la_relation() {
    var user = new User();
    var course = new Course();
    course.setTitle("Java avancé");
    course.setStartDate(LocalDate.of(2026, 9, 1));
    course.setEndDate(LocalDate.of(2026, 12, 1));

    user.enrollTo(course);

    assertThat(user.getCourses()).containsExactly(course);
    assertThat(course.getUsers()).containsExactly(user);
  }

  @Test
  void unenrollFrom_retire_le_cours_des_deux_cotes_de_la_relation() {
    var user = new User();
    var course = new Course();
    course.setTitle("Spring Boot");
    course.setStartDate(LocalDate.of(2027, 1, 10));
    course.setEndDate(LocalDate.of(2027, 3, 10));
    user.enrollTo(course);

    user.unenrollFrom(course);

    assertThat(user.getCourses()).isEmpty();
    assertThat(course.getUsers()).isEmpty();
  }

  @Test
  void enrollTo_permet_l_inscription_a_plusieurs_cours() {
    var user = new User();
    var java = new Course();
    java.setTitle("Java avancé");
    java.setStartDate(LocalDate.of(2026, 9, 1));
    java.setEndDate(LocalDate.of(2026, 12, 1));
    var spring = new Course();
    spring.setTitle("Spring Boot");
    spring.setStartDate(LocalDate.of(2027, 1, 10));
    spring.setEndDate(LocalDate.of(2027, 3, 10));

    user.enrollTo(java);
    user.enrollTo(spring);

    assertThat(user.getCourses()).containsExactlyInAnyOrder(java, spring);
  }
}
