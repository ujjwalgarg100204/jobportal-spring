package com.ujjwalgarg.jobportal.entity;

import com.ujjwalgarg.jobportal.validator.Create;
import com.ujjwalgarg.jobportal.validator.Update;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

/**
 * ContactInformation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "contact_information")
public class ContactInformation {

  @Null(groups = Create.class)
  @NotNull(groups = Update.class)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "phone")
  private String phone;

  @URL(message = "Invalid URL provided")
  @Column(name = "twitter_handle")
  private String twitterHandle;

  @URL(message = "Invalid URL provided")
  @Column(name = "linkedin_handle")
  private String linkedinHandle;

  @URL(message = "Invalid URL provided")
  @Column(name = "github_handle")
  private String githubHandle;
}
