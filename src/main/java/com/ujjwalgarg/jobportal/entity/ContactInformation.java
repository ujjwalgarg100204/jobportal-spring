package com.ujjwalgarg.jobportal.entity;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
