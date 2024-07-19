package com.ujjwalgarg.jobportal.controller.payload.common;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

  private Integer id;

  private String email;

  private LocalDate registrationDate;

  private RoleDto role;
}
