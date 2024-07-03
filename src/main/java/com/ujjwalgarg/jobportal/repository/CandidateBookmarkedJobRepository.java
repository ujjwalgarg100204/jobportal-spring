package com.ujjwalgarg.jobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ujjwalgarg.jobportal.entity.CandidateBookmarkedJob;

/**
 * CandidateBookmarkedJobRepository
 */
@Repository
public interface CandidateBookmarkedJobRepository extends JpaRepository<CandidateBookmarkedJob, Integer> {

    boolean existsByCandidateProfile_IdAndJob_Id(int id, int jobId);

}
