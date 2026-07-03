package com.formation.hei.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID userId) {
        super("Aucun utilisateur trouvé avec l'id " + userId);
    }
}