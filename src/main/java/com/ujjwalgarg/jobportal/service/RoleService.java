package com.ujjwalgarg.jobportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.entity.Role;
import com.ujjwalgarg.jobportal.repository.RoleRepository;

/**
 * RoleService
 */
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public boolean checkIfExistsByName(ERole name) {
        return this.roleRepository.existsByName(name);
    }

    public Role createNew(Role role) {
        return this.roleRepository.save(role);
    }

}
