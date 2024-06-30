package com.ujjwalgarg.jobportal.entity;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.ujjwalgarg.jobportal.constant.EmploymentType;
import com.ujjwalgarg.jobportal.constant.RemoteType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Job
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "Title is required")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Description is required")
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "salary")
    private String salary;

    @NotNull(message = "Employment type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false)
    private EmploymentType employmentType;

    @NotNull(message = "Remote type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "remote_type", nullable = false)
    private RemoteType remoteType;

    @Min(value = 1, message = "No of vacancy must be greater than 0")
    @NotNull(message = "No of vacancy is required")
    @Column(name = "no_of_vacancy", nullable = false)
    private Integer noOfVacancy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDate createdAt;

    @Column(name = "hiring_complete", nullable = false, columnDefinition = "BIT(1) DEFAULT 0")
    private Boolean hiringComplete;

    @NotNull(message = "Recruiter profile is required")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_profile_id", referencedColumnName = "user_id", nullable = false)
    private RecruiterProfile recruiterProfile;

    @OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @NotNull(message = "Company is required")
    @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CandidateJobApplication> candidateJobApplications;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CandidateBookmarkedJob> candidateBookmarkedJobs;

}
