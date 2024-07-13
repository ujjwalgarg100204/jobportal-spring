package com.ujjwalgarg.jobportal.mapper;

import com.ujjwalgarg.jobportal.controller.payload.auth.NewCandidateRequest;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactInformationMapper {

  @Mapping(target = "id", ignore = true)
  ContactInformation fromNewCandidateRequest(NewCandidateRequest request);
}
