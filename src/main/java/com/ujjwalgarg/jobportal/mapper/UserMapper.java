package com.ujjwalgarg.jobportal.mapper;

import com.ujjwalgarg.jobportal.controller.payload.auth.NewCandidateRequest;
import com.ujjwalgarg.jobportal.controller.payload.auth.NewRecruiterRequest;
import com.ujjwalgarg.jobportal.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "email", target = "email")
  @Mapping(source = "password", target = "password")
  @Mapping(target = "registrationDate", ignore = true)
  @Mapping(target = "role", ignore = true)
  User fromNewCandidateRequest(NewCandidateRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "email", target = "email")
  @Mapping(source = "password", target = "password")
  @Mapping(target = "registrationDate", ignore = true)
  @Mapping(target = "role", ignore = true)
  User fromNewRecruiterRequest(NewRecruiterRequest request);
}
