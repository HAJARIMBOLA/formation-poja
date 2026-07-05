package com.formation.hei.repository;

import com.formation.hei.model.User;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {

  private final Map<UUID, User> users = new ConcurrentHashMap<>();

  @Override
  public Optional<User> findById(UUID id) {
    return Optional.ofNullable(users.get(id));
  }

  @Override
  public User save(User user) {
    users.put(user.getId(), user);
    return user;
  }

  @PostConstruct
  void seed() {
    var demoUser =
        new User(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            "Jean",
            "Rakoto",
            "jrakoto",
            "jean.rakoto@example.com");
    users.put(demoUser.getId(), demoUser);
  }
}
