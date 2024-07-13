package com.ujjwalgarg.jobportal.entity;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.validator.Create;
import com.ujjwalgarg.jobportal.validator.Update;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Role
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Role {

  @Id
  @NotNull(groups = {Update.class})
  @Null(groups = {Create.class})
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @NotNull(message = "Name is required", groups = {Create.class, Update.class})
  @Enumerated(EnumType.STRING)
  @Column(name = "name", nullable = false, unique = true)
  private ERole name;

  // TODO: this might be a bug when i go to update user, a will trigger a validation for Update.class
  @NotNull(groups = {Update.class})
  @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<User> users;
}
