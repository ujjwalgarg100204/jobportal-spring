package com.ujjwalgarg.jobportal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * CandidateBookmarkedJob
 */
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
