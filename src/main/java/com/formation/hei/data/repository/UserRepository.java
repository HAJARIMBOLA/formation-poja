package com.formation.hei.data.repository;

import com.formation.hei.data.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUserName(String userName);

  Optional<User> findByEmail(String email);
}
