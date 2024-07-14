package com.ujjwalgarg.jobportal.mapper.impl;

import com.ujjwalgarg.jobportal.entity.Company;
import com.ujjwalgarg.jobportal.mapper.CompanyMapper;
import com.ujjwalgarg.jobportal.mapper.CompanyMapperImpl;
import com.ujjwalgarg.jobportal.util.MapperUtils;
import com.ujjwalgarg.jobportal.validator.validatable.CompanyDetailsValidatable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class CompanyMapperCustomImpl implements CompanyMapper {

  private final CompanyMapperImpl companyMapper;

  @Override
  public Company fromCompanyDetailsValidatable(CompanyDetailsValidatable request) {
    var company = companyMapper.fromCompanyDetailsValidatable(request);
    return MapperUtils.hasAllPropertiesNull(company) ? null : company;
  }
}
