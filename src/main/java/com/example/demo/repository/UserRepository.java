package com.example.demo.repository;

import com.example.demo.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  Optional<User> findById(UUID id);

  User save(User user);
}
