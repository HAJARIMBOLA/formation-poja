package com.formation.hei.model;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;

@Getter
public class User {

  private final UUID id;
  private final String firstName;
  private final String lastName;
  private final String userName;
  private final String email;
  private final Set<UUID> courseIds = ConcurrentHashMap.newKeySet();

  public User(UUID id, String firstName, String lastName, String userName, String email) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userName = userName;
    this.email = email;
  }
}
