package com.ujjwalgarg.jobportal.mapper;

import com.ujjwalgarg.jobportal.controller.payload.common.RoleDto;
import com.ujjwalgarg.jobportal.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

  RoleDto fromRoleToRoleDto(Role role);

}
