package com.ujjwalgarg.jobportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CandidateBookmarkedJob
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "candidate_bookmarked_job")
public class CandidateBookmarkedJob {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @NotNull(message = "Candidate Profile is required")
  @ManyToOne(optional = false)
  private CandidateProfile candidateProfile;

  @NotNull(message = "Job is required")
  @ManyToOne(optional = false)
  private Job job;
}
