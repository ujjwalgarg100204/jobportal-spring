package com.ujjwalgarg.jobportal.mapper;

import com.ujjwalgarg.jobportal.controller.payload.auth.NewRecruiterRequest;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = CompanyMapper.class, componentModel = "spring")
public interface RecruiterProfileMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "firstName", target = "firstName")
  @Mapping(source = "lastName", target = "lastName")
  @Mapping(target = "about", ignore = true)
  @Mapping(target = "hasProfilePhoto", constant = "false")
  @Mapping(target = "contactInformation", ignore = true)
  @Mapping(target = "educations", ignore = true)
  @Mapping(target = "interests", ignore = true)
  @Mapping(target = "jobs", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "company", source = "request")
  RecruiterProfile fromNewRecruiterRequest(NewRecruiterRequest request);
}
