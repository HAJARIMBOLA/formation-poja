package com.example.demo.data.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a user of the platform.
 *
 * <p>A {@link User} can be enrolled in several {@link Course}s, and a {@link Course} can contain
 * several {@link User}s: this is a classic Many-to-Many relationship. {@link User} is the owning
 * side of the relationship, materialized in database by the join table {@code user_course}.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "courses")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "user_name", nullable = false, unique = true)
  private String userName;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @ManyToMany(
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_course",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "course_id"))
  private Set<Course> courses = new HashSet<>();

  /** Enrolls this user to the given course, keeping both sides of the relation in sync. */
  public void enrollTo(Course course) {
    this.courses.add(course);
    course.getUsers().add(this);
  }

  /** Unenrolls this user from the given course, keeping both sides of the relation in sync. */
  public void unenrollFrom(Course course) {
    this.courses.remove(course);
    course.getUsers().remove(this);
  }
}
