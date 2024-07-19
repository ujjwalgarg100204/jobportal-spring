package com.ujjwalgarg.jobportal.mapper;

import com.ujjwalgarg.jobportal.controller.payload.common.UserDto;
import com.ujjwalgarg.jobportal.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface AuthMapper {

  @Mapping(target = "role", source = "role")
  UserDto fromUserToUserDto(User user);
}
