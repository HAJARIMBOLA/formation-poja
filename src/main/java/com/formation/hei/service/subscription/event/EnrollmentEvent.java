package com.formation.hei.service.subscription.event;

import com.formation.hei.model.Course;
import com.formation.hei.model.User;

/**
 * Événement publié une fois qu'un utilisateur a été inscrit à un cours avec succès. Consommé de
 * façon asynchrone par {@link EnrollmentEventListener} pour déclencher l'envoi de l'e-mail de
 * confirmation, sans bloquer la requête HTTP d'origine.
 */
public record EnrollmentEvent(User user, Course course) {}
