package com.ujjwalgarg.jobportal.repository;

import com.ujjwalgarg.jobportal.entity.CandidateBookmarkedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CandidateBookmarkedJobRepository
 */
@Repository
public interface CandidateBookmarkedJobRepository
    extends JpaRepository<CandidateBookmarkedJob, Integer> {

}
