package com.example.demo.service.subscription.event;

import com.example.demo.model.Course;
import com.example.demo.model.User;

/**
 * Événement publié une fois qu'un utilisateur a été inscrit à un cours avec succès. Consommé de
 * façon asynchrone par {@link EnrollmentEventListener} pour déclencher l'envoi de l'e-mail de
 * confirmation, sans bloquer la requête HTTP d'origine.
 */
public record EnrollmentEvent(User user, Course course) {}
