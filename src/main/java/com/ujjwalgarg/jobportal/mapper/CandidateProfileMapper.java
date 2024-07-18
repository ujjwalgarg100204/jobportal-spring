package com.ujjwalgarg.jobportal.mapper;

import com.ujjwalgarg.jobportal.controller.payload.auth.NewCandidateRequest;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.service.dto.candidateprofileservice.CandidateProfileUpdateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {
    ContactInformationMapper.class})
public interface CandidateProfileMapper {

  @Mapping(source = "firstName", target = "firstName")
  @Mapping(source = "lastName", target = "lastName")
  @Mapping(source = "shortAbout", target = "shortAbout")
  @Mapping(target = "hasProfilePhoto", constant = "false")
  @Mapping(target = "hasResume", constant = "false")
  @Mapping(target = "contactInformation", source = "request")
  CandidateProfile fromNewCandidateRequest(NewCandidateRequest request);


  @Mapping(target = "user", ignore = true)
  @Mapping(target = "jobApplications", ignore = true)
  @Mapping(target = "bookmarkedJobs", ignore = true)
  @Mapping(target = "hasProfilePhoto", ignore = true)
  @Mapping(target = "hasResume", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateCandidateProfileFromDTO(CandidateProfileUpdateDTO dto,
      @MappingTarget CandidateProfile entity);
}
