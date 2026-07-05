package com.formation.hei.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryCourseRepositoryTest {

  private InMemoryCourseRepository courseRepository;

  @BeforeEach
  void setUp() {
    courseRepository = new InMemoryCourseRepository();
    courseRepository.seed();
  }

  @Test
  void trouve_le_cours_de_demo_apres_le_seed() {
    var demoCourseId = UUID.fromString("22222222-2222-2222-2222-222222222222");

    var found = courseRepository.findById(demoCourseId);

    assertThat(found).isPresent();
    assertThat(found.get().getTitle()).isEqualTo("Formation Spring Boot Avancé");
    assertThat(found.get().getStartDate()).isBefore(found.get().getEndDate());
  }

  @Test
  void retourne_vide_si_le_cours_n_existe_pas() {
    var found = courseRepository.findById(UUID.randomUUID());

    assertThat(found).isEmpty();
  }
}
