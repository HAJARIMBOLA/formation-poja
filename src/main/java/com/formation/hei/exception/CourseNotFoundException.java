package com.formation.hei.exception;

import java.util.UUID;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(UUID courseId) {
        super("Aucun cours trouvé avec l'id " + courseId);
    }
}