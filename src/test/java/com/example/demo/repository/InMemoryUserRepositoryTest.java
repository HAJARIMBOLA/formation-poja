package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.model.User;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryUserRepositoryTest {

  private InMemoryUserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository = new InMemoryUserRepository();
    userRepository.seed();
  }

  @Test
  void trouve_l_utilisateur_de_demo_apres_le_seed() {
    var demoUserId = UUID.fromString("11111111-1111-1111-1111-111111111111");

    var found = userRepository.findById(demoUserId);

    assertThat(found).isPresent();
    assertThat(found.get().getFirstName()).isEqualTo("Jean");
    assertThat(found.get().getLastName()).isEqualTo("Rakoto");
  }

  @Test
  void retourne_vide_si_l_utilisateur_n_existe_pas() {
    var found = userRepository.findById(UUID.randomUUID());

    assertThat(found).isEmpty();
  }

  @Test
  void sauvegarde_puis_retrouve_un_utilisateur() {
    var user = new User(UUID.randomUUID(), "Marie", "Rasoa", "mrasoa", "marie.rasoa@example.com");

    var saved = userRepository.save(user);
    var found = userRepository.findById(user.getId());

    assertThat(saved).isEqualTo(user);
    assertThat(found).contains(user);
  }

  @Test
  void ecrase_un_utilisateur_existant_avec_le_meme_id() {
    var id = UUID.randomUUID();
    var original = new User(id, "Marie", "Rasoa", "mrasoa", "marie.rasoa@example.com");
    var updated = new User(id, "Marie", "Rasoa", "mrasoa2", "marie.rasoa2@example.com");

    userRepository.save(original);
    userRepository.save(updated);

    assertThat(userRepository.findById(id)).contains(updated);
  }
}
