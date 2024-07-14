package com.ujjwalgarg.jobportal.mapper;

import com.ujjwalgarg.jobportal.entity.Company;
import com.ujjwalgarg.jobportal.validator.validatable.CompanyDetailsValidatable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface CompanyMapper {

  @Mapping(target = "address", source = "request")
  @Mapping(target = "recruiterProfile", ignore = true)
  @Mapping(target = "jobs", ignore = true)
  @Mapping(target = "id", source = "companyId")
  @Mapping(target = "name", source = "companyName")
  @Mapping(target = "hasLogo", constant = "false")
  Company fromCompanyDetailsValidatable(CompanyDetailsValidatable request);
}
