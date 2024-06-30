package com.ujjwalgarg.jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ujjwalgarg.jobportal.controller.payload.response.GetRecruiterJobsResponse;
import com.ujjwalgarg.jobportal.entity.Job;

/**
 * JobRepository
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

    @Query(value = "SELECT j.id              as id, " +
            "j.title                         as title, " +
            "j.description                   as description, " +
            "j.salary                        as salary, " +
            "j.employment_type               as employmentType, " +
            "j.remote_type                   as remoteType, " +
            "j.no_of_vacancy                 as noOfVacancy, " +
            "j.created_at                    as createdAt, " +
            "j.hiring_complete               as hiringComplete, " +
            "a.id as addressId, " +
            "a.city as addressCity, " +
            "a.country as addressCountry, " +
            "a.state as addressState, " +
            "COUNT(cja.candidate_profile_id) as noOfApplicants " +
            "FROM jobportal.job j " +
            "INNER JOIN jobportal.address a on j.address_id = a.id " +
            "LEFT JOIN jobportal.candidate_job_application cja on j.id = cja.job_id " +
            "WHERE j.recruiter_profile_id = :recruiter_id " +
            "GROUP BY j.id", nativeQuery = true)
    List<GetRecruiterJobsResponse> findAllByRecruiterIdWithApplicantCount(@Param("recruiter_id") Integer recruiterId);

}
