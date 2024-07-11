package com.ujjwalgarg.jobportal.mapper;

import com.ujjwalgarg.jobportal.controller.payload.auth.RegisterNewRecruiterRequest;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import org.mapstruct.Mapper;

@Mapper(uses = CompanyMapper.class)
public interface RecruiterProfileMapper {

  RecruiterProfile toRecruiterProfile(RegisterNewRecruiterRequest registerNewRecruiterRequest);

}
