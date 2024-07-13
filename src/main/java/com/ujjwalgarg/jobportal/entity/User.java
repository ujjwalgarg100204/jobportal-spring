package com.ujjwalgarg.jobportal.entity;

import com.ujjwalgarg.jobportal.validator.Create;
import com.ujjwalgarg.jobportal.validator.Update;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * User
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  @NotNull(groups = Update.class)
  @Null(groups = Create.class)
  private Integer id;

  @NotBlank(message = "Email is required", groups = Create.class)
  @Null(groups = Update.class)
  @Column(name = "email", unique = true, updatable = false, nullable = false)
  private String email;

  @NotBlank(message = "Password is required", groups = Create.class)
  @Column(name = "password", nullable = false)
  private String password;

  @Null(groups = {Create.class, Update.class})
  @DateTimeFormat(pattern = "YYYY-mm-dd")
  @CreationTimestamp
  @Column(
      name = "registration_date",
      nullable = false,
      updatable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDate registrationDate;

  @NotNull(message = "Role is required", groups = {Create.class, Update.class})
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id", referencedColumnName = "id")
  private Role role;
}
