package com.ujjwalgarg.jobportal.service;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.entity.Role;

/**
 * RoleService
 */
public interface RoleService {

  Role getRoleByName(ERole name);

  boolean checkIfExistsByName(ERole name);

  Role createNew(Role role);

}
