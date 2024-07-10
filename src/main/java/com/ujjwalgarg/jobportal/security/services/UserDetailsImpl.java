package com.ujjwalgarg.jobportal.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ujjwalgarg.jobportal.entity.User;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Implementation of {@link UserDetails} interface. This class represents the user details required
 * for Spring Security authentication and authorization. It contains user information such as id,
 * email, password, and authorities.
 */
@Builder
@Getter
public class UserDetailsImpl implements UserDetails {

  private int id;

  private String email;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  /**
   * Builds a {@link UserDetailsImpl} instance from a {@link User} entity. Converts the user's roles
   * to {@link GrantedAuthority}.
   *
   * @param user the {@link User} entity
   * @return a {@link UserDetailsImpl} instance
   */
  public static UserDetailsImpl buildFromUser(User user) {
    List<SimpleGrantedAuthority> authorities =
        List.of(new SimpleGrantedAuthority(user.getRole().getName().toString()));

    return UserDetailsImpl.builder()
        .id(user.getId())
        .email(user.getEmail())
        .password(user.getPassword())
        .authorities(authorities)
        .build();
  }

  /**
   * Returns the authorities granted to the user.
   *
   * @return a collection of {@link GrantedAuthority} objects
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((password == null) ? 0 : password.hashCode());
    result = prime * result + ((authorities == null) ? 0 : authorities.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    UserDetailsImpl other = (UserDetailsImpl) obj;
    if (id != other.id) {
      return false;
    }
    if (email == null) {
      if (other.email != null) {
        return false;
      }
    } else if (!email.equals(other.email)) {
      return false;
    }
    if (password == null) {
      if (other.password != null) {
        return false;
      }
    } else if (!password.equals(other.password)) {
      return false;
    }
    if (authorities == null) {
      return other.authorities == null;
    } else {
      return authorities.equals(other.authorities);
    }
  }
}
