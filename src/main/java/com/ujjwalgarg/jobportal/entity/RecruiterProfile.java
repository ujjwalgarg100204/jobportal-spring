package com.ujjwalgarg.jobportal.entity;

import com.ujjwalgarg.jobportal.validator.Create;
import com.ujjwalgarg.jobportal.validator.Update;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "recruiter_profile")
public class RecruiterProfile {

  @Id
  @NotNull(groups = Update.class)
  @Null(groups = Create.class)
  private Integer id;

  @NotBlank(message = "First Name is mandatory", groups = {Create.class, Update.class})
  @Column(name = "first_name", nullable = false)
  private String firstName;

  @NotBlank(message = "Last Name is mandatory", groups = {Create.class, Update.class})
  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Length(max = 10_000, message = "Maximum number of characters allowed is 10,000")
  @Column(name = "about", length = 10_000)
  private String about;

  @Default
  @Column(name = "has_profile_photo", nullable = false, columnDefinition = "BIT(1) DEFAULT 0")
  private Boolean hasProfilePhoto = false;

  @NotNull(groups = {Update.class})
  @OneToOne(cascade = CascadeType.ALL)
  @MapsId
  private User user;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private Address address;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "contact_information_id", referencedColumnName = "id")
  private ContactInformation contactInformation;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "recruiter_education",
      joinColumns = @JoinColumn(name = "recruiter_id"),
      inverseJoinColumns = @JoinColumn(name = "id"))
  private List<Education> educations;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "recruiter_interest",
      joinColumns = @JoinColumn(name = "recruiter_id"),
      inverseJoinColumns = @JoinColumn(name = "id"))
  private List<Interest> interests;

  @NotNull(message = "Company must be defined for a recruiter", groups = {Create.class,
      Update.class})
  @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
      CascadeType.REFRESH}, optional = false)
  @JoinColumn(name = "company_id", referencedColumnName = "id")
  private Company company;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "recruiterProfile")
  private List<Job> jobs;
}
