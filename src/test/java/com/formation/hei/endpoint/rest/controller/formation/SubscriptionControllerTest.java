package com.formation.hei.endpoint.rest.controller.formation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.formation.hei.endpoint.rest.controller.subscription.SubscriptionController;
import com.formation.hei.endpoint.rest.model.SubscriptionResponse;
import com.formation.hei.exception.AlreadySubscribedException;
import com.formation.hei.exception.UserNotFoundException;
import com.formation.hei.service.subscription.SubscriptionService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SubscriptionController.class)
class SubscriptionControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private SubscriptionService subscriptionService;

  @Test
  void retourne_201_avec_le_message_de_confirmation() throws Exception {
    var userId = UUID.randomUUID();
    var courseId = UUID.randomUUID();
    var response =
        new SubscriptionResponse(userId, courseId, "Inscription confirmée pour Jean au cours X");
    when(subscriptionService.subscribe(userId, courseId)).thenReturn(response);

    mockMvc
        .perform(post("/users/{userId}/courses/{courseId}", userId, courseId))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").value(userId.toString()))
        .andExpect(jsonPath("$.courseId").value(courseId.toString()));
  }

  @Test
  void retourne_404_si_l_utilisateur_n_existe_pas() throws Exception {
    var userId = UUID.randomUUID();
    var courseId = UUID.randomUUID();
    when(subscriptionService.subscribe(any(UUID.class), any(UUID.class)))
        .thenThrow(new UserNotFoundException(userId));

    mockMvc
        .perform(post("/users/{userId}/courses/{courseId}", userId, courseId))
        .andExpect(status().isNotFound());
  }

  @Test
  void retourne_409_si_deja_inscrit() throws Exception {
    var userId = UUID.randomUUID();
    var courseId = UUID.randomUUID();
    when(subscriptionService.subscribe(any(UUID.class), any(UUID.class)))
        .thenThrow(new AlreadySubscribedException(userId, courseId));

    mockMvc
        .perform(post("/users/{userId}/courses/{courseId}", userId, courseId))
        .andExpect(status().isConflict());
  }
}
