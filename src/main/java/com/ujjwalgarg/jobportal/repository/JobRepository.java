package com.ujjwalgarg.jobportal.repository;

import com.ujjwalgarg.jobportal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JobRepository
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

}
