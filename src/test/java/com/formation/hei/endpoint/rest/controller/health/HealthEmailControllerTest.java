package com.formation.hei.endpoint.rest.controller.health;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.formation.hei.mail.Email;
import com.formation.hei.mail.Mailer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HealthEmailController.class)
class HealthEmailControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private Mailer mailer;

  @Test
  void envoie_les_cinq_emails_de_verification_et_retourne_200() throws Exception {
    mockMvc
        .perform(get("/health/email").param("to", "participant@example.com"))
        .andExpect(status().isOk())
        .andExpect(content().string("OK"));

    verify(mailer, times(5)).accept(any(Email.class));
  }
}
