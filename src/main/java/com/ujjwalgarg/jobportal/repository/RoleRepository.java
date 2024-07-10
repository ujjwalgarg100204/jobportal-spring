package com.ujjwalgarg.jobportal.repository;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * RoleRepository
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

  boolean existsByName(ERole name);

  Optional<Role> findByName(ERole name);
}
