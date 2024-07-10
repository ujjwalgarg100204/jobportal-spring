package com.ujjwalgarg.jobportal.repository;

import com.ujjwalgarg.jobportal.entity.CandidateJobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateJobApplicationRepository
    extends JpaRepository<CandidateJobApplication, Integer> {

}
