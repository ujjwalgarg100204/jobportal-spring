package com.ujjwalgarg.jobportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Education
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "education")
public class Education {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @NotBlank(message = "Title is mandatory")
  @Column(name = "title", nullable = false)
  private String title;

  @NotBlank(message = "Description is mandatory")
  @Column(name = "description", nullable = false)
  private String description;
}
