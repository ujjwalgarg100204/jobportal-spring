package com.ujjwalgarg.jobportal.mapper;

import com.ujjwalgarg.jobportal.controller.payload.auth.NewRecruiterRequest;
import com.ujjwalgarg.jobportal.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "companyAddressCity", target = "city")
  @Mapping(source = "companyAddressState", target = "state")
  @Mapping(source = "companyAddressCountry", target = "country")
  Address fromNewRecruiterRequest(NewRecruiterRequest request);
}
