package com.ujjwalgarg.jobportal.mapper.impl;

import com.ujjwalgarg.jobportal.controller.payload.auth.NewCandidateRequest;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import com.ujjwalgarg.jobportal.mapper.ContactInformationMapper;
import com.ujjwalgarg.jobportal.mapper.ContactInformationMapperImpl;
import com.ujjwalgarg.jobportal.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class ContactInformationMapperCustomImpl implements
    ContactInformationMapper {

  private final ContactInformationMapperImpl contactInformationMapper;

  @Override
  public ContactInformation fromNewCandidateRequest(NewCandidateRequest request) {
    ContactInformation contactInformation = contactInformationMapper.fromNewCandidateRequest(
        request);
    return MapperUtils.hasAllPropertiesNull(contactInformation) ? null : contactInformation;
  }
}
