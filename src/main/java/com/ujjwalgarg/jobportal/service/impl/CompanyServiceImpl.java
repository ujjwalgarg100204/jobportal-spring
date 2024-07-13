package com.ujjwalgarg.jobportal.service.impl;

import com.ujjwalgarg.jobportal.entity.Company;
import com.ujjwalgarg.jobportal.exception.NotFoundException;
import com.ujjwalgarg.jobportal.repository.CompanyRepository;
import com.ujjwalgarg.jobportal.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j(topic = "COMPANY_SERVICE")
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

  private final CompanyRepository companyRepository;

  @Override
  public Company getCompanyById(int id) throws NotFoundException {
    return companyRepository.findById(id).orElseThrow(() -> {
      log.warn("Company with id:{} not found", id);
      return new NotFoundException("Company with id:" + id + " not found");
    });
  }
}
