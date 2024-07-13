package com.ujjwalgarg.jobportal.entity;

import com.ujjwalgarg.jobportal.constant.ExperienceLevel;
import com.ujjwalgarg.jobportal.validator.Create;
import com.ujjwalgarg.jobportal.validator.Update;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Skill
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "skill")
public class Skill {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  @Null(groups = Create.class)
  @NotNull(groups = Update.class)
  private Integer id;

  @NotBlank(message = "Name of skill is mandatory", groups = {Create.class, Update.class})
  @Column(name = "name", nullable = false)
  private String name;

  @NotBlank(message = "Years of Experience of skill is mandatory", groups = {Create.class,
      Update.class})
  @Column(name = "years_of_experience", nullable = false)
  private String yearsOfExperience;

  @NotNull(message = "Experience level of skill is mandatory", groups = {Create.class,
      Update.class})
  @Enumerated(EnumType.STRING)
  @Column(name = "experience_level", nullable = false)
  private ExperienceLevel experienceLevel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "candidate_profile_id", referencedColumnName = "user_id")
  private CandidateProfile candidateProfile;
}
