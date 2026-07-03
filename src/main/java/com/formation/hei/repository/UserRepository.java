package com.formation.hei.repository;

import com.formation.hei.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findById(UUID id);

    User save(User user);
}