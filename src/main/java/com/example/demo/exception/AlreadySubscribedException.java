package com.example.demo.exception;

import java.util.UUID;

public class AlreadySubscribedException extends RuntimeException {

  public AlreadySubscribedException(UUID userId, UUID courseId) {
    super("L'utilisateur " + userId + " est déjà inscrit au cours " + courseId);
  }
}
