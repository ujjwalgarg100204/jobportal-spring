package com.ujjwalgarg.jobportal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ujjwalgarg.jobportal.entity.Company;
import com.ujjwalgarg.jobportal.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

/**
 * CompanyService
 */
@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company createNew(Company company) {
        return companyRepository.save(company);
    }

    public Company updateJob(Company company) {
        return companyRepository.save(company);
    }

    public Optional<Company> findById(Integer id) {
        return companyRepository.findById(id);
    }

    public static String getCompanyLogoS3Path(int id) {
        return String.format("company-logo/%d", id);
    }

}
