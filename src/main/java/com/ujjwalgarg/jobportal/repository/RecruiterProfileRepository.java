package com.ujjwalgarg.jobportal.repository;

import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * RecruiterProfileRepository
 */
@Repository
public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Integer> {

  Optional<RecruiterProfile> findById(int id);
}
