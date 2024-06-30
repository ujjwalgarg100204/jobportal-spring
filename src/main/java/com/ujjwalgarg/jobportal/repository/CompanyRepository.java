package com.ujjwalgarg.jobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ujjwalgarg.jobportal.entity.Company;

/**
 * CompanyRepository
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

}
