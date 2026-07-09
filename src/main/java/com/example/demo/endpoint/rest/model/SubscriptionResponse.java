package com.example.demo.endpoint.rest.model;

import java.util.UUID;

public record SubscriptionResponse(UUID userId, UUID courseId, String message) {}
