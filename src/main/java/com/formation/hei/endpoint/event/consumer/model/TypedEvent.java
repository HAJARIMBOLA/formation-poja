package com.formation.hei.endpoint.event.consumer.model;

import com.formation.hei.PojaGenerated;
import com.formation.hei.endpoint.event.model.PojaEvent;

@PojaGenerated
public record TypedEvent(String typeName, PojaEvent payload) {}
