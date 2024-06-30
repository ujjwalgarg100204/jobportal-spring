package com.ujjwalgarg.jobportal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.entity.Role;
import com.ujjwalgarg.jobportal.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class JobportalApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobportalApplication.class, args);
    }

    @Bean
    @Transactional
    public CommandLineRunner createUserRoles(RoleService roleService) {
        return runner -> {
            Role candidateRole = Role.builder()
                    .id(1)
                    .name(ERole.ROLE_CANDIDATE)
                    .build();
            Role recruiterRole = Role.builder()
                    .id(2)
                    .name(ERole.ROLE_RECRUITER)
                    .build();

            if (!roleService.checkIfExistsByName(candidateRole.getName())) {
                log.info("Created *Candidate* role in database");
                roleService.createNew(candidateRole);
            }
            if (!roleService.checkIfExistsByName(recruiterRole.getName())) {
                log.info("Created *Recruiter* role in database");
                roleService.createNew(recruiterRole);
            }

        };
    }
}
