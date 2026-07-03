package com.formation.hei.endpoint.rest.controller.formation;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.hei.endpoint.rest.model.InscriptionRequest;
import com.formation.hei.endpoint.rest.model.InscriptionResponse;
import com.formation.hei.service.formation.InscriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FormationInscriptionController.class)
class FormationInscriptionControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private InscriptionService inscriptionService;

  @Test
  void retourne_201_avec_le_message_de_confirmation() throws Exception {
    var request =
        new InscriptionRequest("Spring Boot avancé", "Jean Dupont", "jean.dupont@example.com");
    var response =
        new InscriptionResponse(
            "Inscription confirmée pour Jean Dupont à la formation Spring Boot avancé");
    when(inscriptionService.inscrire(any(InscriptionRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/formations/inscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message", containsString("Jean Dupont")));
  }

  @Test
  void retourne_400_si_les_donnees_sont_invalides() throws Exception {
    var request = new InscriptionRequest("", "Jean Dupont", "jean.dupont@example.com");
    when(inscriptionService.inscrire(any(InscriptionRequest.class)))
        .thenThrow(new IllegalArgumentException("Le titre de la formation est obligatoire"));

    mockMvc
        .perform(
            post("/formations/inscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
