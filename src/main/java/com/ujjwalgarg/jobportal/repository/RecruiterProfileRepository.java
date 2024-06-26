package com.ujjwalgarg.jobportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ujjwalgarg.jobportal.entity.RecruiterProfile;

/**
 * RecruiterProfileRepository
 */
@Repository
public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Integer> {

    Optional<RecruiterProfile> findById(int id);

}
