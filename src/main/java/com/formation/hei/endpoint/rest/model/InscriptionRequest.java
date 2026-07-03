package com.formation.hei.endpoint.rest.model;

public record InscriptionRequest(
    String formationTitle, String participantName, String participantEmail) {}
