package com.example.demo.service.formation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.example.demo.endpoint.rest.model.InscriptionRequest;
import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InscriptionServiceTest {

  @Mock private Mailer mailer;

  private InscriptionService inscriptionService;

  @BeforeEach
  void setUp() {
    inscriptionService = new InscriptionService(mailer);
  }

  @Test
  void inscrit_un_participant_et_envoie_un_email_de_confirmation() {
    var request =
        new InscriptionRequest("Spring Boot avancé", "Jean Dupont", "jean.dupont@example.com");

    var response = inscriptionService.inscrire(request);

    assertThat(response.message())
        .isEqualTo("Inscription confirmée pour Jean Dupont à la formation Spring Boot avancé");
    verify(mailer).accept(any(Email.class));
  }

  @Test
  void rejette_une_inscription_sans_titre_de_formation() {
    var request = new InscriptionRequest("", "Jean Dupont", "jean.dupont@example.com");

    assertThatThrownBy(() -> inscriptionService.inscrire(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Le titre de la formation est obligatoire");
  }

  @Test
  void rejette_une_inscription_sans_nom_de_participant() {
    var request = new InscriptionRequest("Spring Boot avancé", "  ", "jean.dupont@example.com");

    assertThatThrownBy(() -> inscriptionService.inscrire(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Le nom du participant est obligatoire");
  }

  @Test
  void rejette_une_inscription_sans_email() {
    var request = new InscriptionRequest("Spring Boot avancé", "Jean Dupont", null);

    assertThatThrownBy(() -> inscriptionService.inscrire(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("L'adresse e-mail du participant est obligatoire");
  }

  @Test
  void rejette_une_adresse_email_invalide() {
    var request = new InscriptionRequest("Spring Boot avancé", "Jean Dupont", "not an email");

    assertThatThrownBy(() -> inscriptionService.inscrire(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("L'adresse e-mail du participant est invalide");
  }
}
