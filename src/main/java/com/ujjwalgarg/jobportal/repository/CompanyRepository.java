package com.ujjwalgarg.jobportal.repository;

import com.ujjwalgarg.jobportal.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CompanyRepository
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

}
