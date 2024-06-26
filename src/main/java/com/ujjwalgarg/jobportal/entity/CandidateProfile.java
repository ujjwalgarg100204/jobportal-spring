package com.ujjwalgarg.jobportal.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import com.ujjwalgarg.jobportal.constant.EmploymentType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CandidateProfile
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "candidate_profile")
public class CandidateProfile {

    @Id
    private Integer id;

    @NotBlank(message = "First Name is mandatory")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last Name is mandatory")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Short About is mandatory")
    @Column(name = "short_about", nullable = false)
    private String shortAbout;

    @Length(max = 10_000, message = "Maximum number of characters allowed is 10,000")
    @Column(name = "about", length = 10_000)
    private String about;

    @Column(name = "has_profile_photo", nullable = false, columnDefinition = "BIT(1) DEFAULT 0")
    private Boolean hasProfilePhoto;

    @Column(name = "has_resume", nullable = false, columnDefinition = "BIT(1) DEFAULT 0")
    private Boolean hasResume;

    @URL
    @Column(name = "portfolio_website")
    private String portfolioWebsite;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_employment_type")
    private EmploymentType preferredEmploymentType;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "contact_information_id", referencedColumnName = "id")
    private ContactInformation contactInformation;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "candidate_education", joinColumns = @JoinColumn(name = "candidate_id"), inverseJoinColumns = @JoinColumn(name = "id"))
    private List<Education> educations;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "candidate_interest", joinColumns = @JoinColumn(name = "candidate_id"), inverseJoinColumns = @JoinColumn(name = "id"))
    private List<Interest> interests;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "candidateProfile")
    private List<Skill> skills;

}