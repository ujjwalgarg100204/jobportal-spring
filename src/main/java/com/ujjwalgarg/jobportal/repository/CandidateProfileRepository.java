package com.ujjwalgarg.jobportal.repository;

import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CandidateProfileRepository
 */
@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Integer> {

}
