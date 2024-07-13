package com.ujjwalgarg.jobportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * CandidateJobApplications
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "candidate_job_application")
public class CandidateJobApplication {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @NotNull(message = "Candidate Profile is required")
  @ManyToOne(optional = false)
  @JoinColumn(name = "candidate_profile_id", referencedColumnName = "user_id")
  private CandidateProfile candidateProfile;

  @NotNull(message = "Job is required")
  @ManyToOne(optional = false)
  @JoinColumn(name = "job_id", referencedColumnName = "id")
  private Job job;

  @Length(min = 20, max = 10_000, message = "Cover Letter must be between 20 and 10,000 characters")
  @NotBlank(message = "Cover Letter is required")
  @Column(name = "cover_letter", length = 10_000, nullable = false, updatable = false)
  private String coverLetter;
}
