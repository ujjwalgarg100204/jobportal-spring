package com.ujjwalgarg.jobportal.entity;

import com.ujjwalgarg.jobportal.constant.ExperienceLevel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private Integer id;

    @NotBlank(message = "Name of skill is mandatory")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Years of Experience of skill is mandatory")
    @Column(name = "years_of_experience", nullable = false)
    private String yearsOfExperience;

    @NotNull(message = "Experience level of skill is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level", nullable = false)
    private ExperienceLevel experienceLevel;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "candidate_profile_id", referencedColumnName = "user_id")
    private CandidateProfile candidateProfile;

}
