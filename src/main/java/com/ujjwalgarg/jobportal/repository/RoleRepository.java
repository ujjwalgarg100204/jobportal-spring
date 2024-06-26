package com.ujjwalgarg.jobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.entity.Role;

/**
 * RoleRepository
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    boolean existsByName(ERole name);

}
