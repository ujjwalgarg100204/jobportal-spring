package com.ujjwalgarg.jobportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Company
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "company")
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @NotBlank(message = "Name of company is mandatory")
  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @Default
  @Column(name = "has_logo", nullable = false, columnDefinition = "BIT(1) DEFAULT 0")
  private Boolean hasLogo = false;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private Address address;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "company", fetch = FetchType.LAZY)
  private List<RecruiterProfile> recruiterProfile;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "company", fetch = FetchType.LAZY)
  private List<Job> jobs;
}
