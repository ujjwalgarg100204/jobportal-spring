package com.ujjwalgarg.jobportal.entity;

import com.ujjwalgarg.jobportal.validator.Create;
import com.ujjwalgarg.jobportal.validator.Update;
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

  @Null(groups = Create.class)
  @NotNull(groups = Update.class)
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
