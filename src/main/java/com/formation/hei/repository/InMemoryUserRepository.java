package com.formation.hei.repository;

import com.formation.hei.model.User;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

// TODO: remplacer par une implémentation JPA/Postgres quand la couche data sera en place.
// L'interface UserRepository ne change pas, seul ce composant sera remplacé.
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
        // Données de démo pour tester l'endpoint sans dépendre d'une base de données.
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