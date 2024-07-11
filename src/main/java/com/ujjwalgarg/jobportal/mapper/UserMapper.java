package com.ujjwalgarg.jobportal.mapper;

import com.ujjwalgarg.jobportal.controller.payload.auth.RegisterNewRecruiterRequest;
import com.ujjwalgarg.jobportal.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

  User toUser(RegisterNewRecruiterRequest registerNewRecruiterRequest);
}
