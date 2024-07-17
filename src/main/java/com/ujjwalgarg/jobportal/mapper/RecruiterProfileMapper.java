package com.ujjwalgarg.jobportal.mapper;

import com.ujjwalgarg.jobportal.controller.payload.auth.NewRecruiterRequest;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import com.ujjwalgarg.jobportal.service.dto.recruiterprofileservice.RecruiterProfileUpdateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(uses = CompanyMapper.class, componentModel = "spring")
public interface RecruiterProfileMapper {

  @Mapping(target = "address", ignore = true)
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

  @Mapping(target = "company", source = "source")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "jobs", ignore = true)
  @Mapping(target = "hasProfilePhoto", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateRecruiterProfile(RecruiterProfileUpdateDTO source,
      @MappingTarget RecruiterProfile target);
}
