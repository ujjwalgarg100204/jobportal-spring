package com.ujjwalgarg.jobportal.mapper;

import com.ujjwalgarg.jobportal.controller.payload.auth.RegisterNewRecruiterRequest;
import com.ujjwalgarg.jobportal.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CompanyMapper {

  @Mapping(target = "id", source = "companyId")
  @Mapping(target = "name", source = "companyName")
  @Mapping(target = "address.city", source = "companyAddressCity")
  @Mapping(target = "address.state", source = "companyAddressState")
  @Mapping(target = "address.country", source = "companyAddressCountry")
  Company toCompany(RegisterNewRecruiterRequest request);

}
