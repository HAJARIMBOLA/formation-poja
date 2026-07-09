package com.example.demo.model;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Course {

  private final UUID id;
  private final String title;
  private final Instant startDate;
  private final Instant endDate;

  public Course(UUID id, String title, Instant startDate, Instant endDate) {
    this.id = id;
    this.title = title;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}
