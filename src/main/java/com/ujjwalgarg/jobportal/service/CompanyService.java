package com.ujjwalgarg.jobportal.service;

import com.ujjwalgarg.jobportal.entity.Company;
import com.ujjwalgarg.jobportal.exception.NotFoundException;

public interface CompanyService {

  Company getCompanyById(int id) throws NotFoundException;
}
