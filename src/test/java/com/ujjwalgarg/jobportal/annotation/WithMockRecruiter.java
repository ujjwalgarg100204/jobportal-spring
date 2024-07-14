package com.ujjwalgarg.jobportal.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithMockUser;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "recruiter@gmail.com", roles = "RECRUITER")
public @interface WithMockRecruiter {

}
