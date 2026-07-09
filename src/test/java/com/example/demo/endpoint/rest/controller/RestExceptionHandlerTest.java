package com.example.demo.endpoint.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.exception.AlreadySubscribedException;
import com.example.demo.exception.CourseNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class RestExceptionHandlerTest {

  private RestExceptionHandler restExceptionHandler;

  @BeforeEach
  void setUp() {
    restExceptionHandler = new RestExceptionHandler();
  }

  @Test
  void handleIllegalArgument_retourne_400_avec_le_message() {
    var exception = new IllegalArgumentException("Le titre de la formation est obligatoire");

    var response = restExceptionHandler.handleIllegalArgument(exception);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo("Le titre de la formation est obligatoire");
  }

  @Test
  void handleNotFound_retourne_404_pour_un_utilisateur_introuvable() {
    var userId = UUID.randomUUID();
    var exception = new UserNotFoundException(userId);

    var response = restExceptionHandler.handleNotFound(exception);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).contains(userId.toString());
  }

  @Test
  void handleNotFound_retourne_404_pour_un_cours_introuvable() {
    var courseId = UUID.randomUUID();
    var exception = new CourseNotFoundException(courseId);

    var response = restExceptionHandler.handleNotFound(exception);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).contains(courseId.toString());
  }

  @Test
  void handleAlreadySubscribed_retourne_409_avec_le_message() {
    var userId = UUID.randomUUID();
    var courseId = UUID.randomUUID();
    var exception = new AlreadySubscribedException(userId, courseId);

    var response = restExceptionHandler.handleAlreadySubscribed(exception);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getBody()).contains(userId.toString()).contains(courseId.toString());
  }
}
