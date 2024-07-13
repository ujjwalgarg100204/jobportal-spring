package com.ujjwalgarg.jobportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Address
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "city")
  private String city;

  @NotBlank(message = "State is mandatory")
  @Column(name = "state", nullable = false)
  private String state;

  @NotBlank(message = "Country is mandatory")
  @Column(name = "country", nullable = false)
  private String country;
}
