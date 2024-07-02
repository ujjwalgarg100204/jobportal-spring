package com.ujjwalgarg.jobportal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ujjwalgarg.jobportal.controller.payload.response.GetRecruiterJobsResponse;
import com.ujjwalgarg.jobportal.entity.Job;
import com.ujjwalgarg.jobportal.mapper.JobMapper;
import com.ujjwalgarg.jobportal.repository.JobRepository;

import lombok.RequiredArgsConstructor;

/**
 * JobService
 */
@RequiredArgsConstructor
@Service
public class JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    public Job createNew(Job job) {
        return jobRepository.save(job);
    }

    public Job updateJob(Job job) {
        return jobRepository.save(job);
    }

    public Optional<Job> getJobById(int id) {
        return jobRepository.findById(id);
    }

    public List<GetRecruiterJobsResponse> getJobsOfRecruiterWithApplicantCount(Integer recruiterId) {
        return jobRepository.findAllByRecruiterIdWithApplicantCount(recruiterId);
    }

    public JobMapper getMapper() {
        return jobMapper;
    }

}
