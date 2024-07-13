package com.ujjwalgarg.jobportal.entity;

import com.ujjwalgarg.jobportal.validator.Create;
import com.ujjwalgarg.jobportal.validator.Update;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Interest
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "interest")
public class Interest {

  @Id
  @NotNull(groups = Update.class)
  @Null(groups = Create.class)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @NotBlank(message = "Interest can't be empty")
  @Column(name = "title", nullable = false)
  private String title;
}
