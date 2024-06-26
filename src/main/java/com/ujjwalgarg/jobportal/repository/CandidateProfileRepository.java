package com.ujjwalgarg.jobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ujjwalgarg.jobportal.entity.CandidateProfile;

/**
 * CandidateProfileRepository
 */
@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Integer> {

}
