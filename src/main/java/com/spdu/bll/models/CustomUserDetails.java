package com.spdu.bll.models;

import com.spdu.bll.models.constants.UserRole;
import com.spdu.domain_models.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CustomUserDetails implements UserDetails {

    private final User user;
    private final UserRole userRole;

    public CustomUserDetails(User user, UserRole userRole) {
        this.user = user;
        this.userRole = userRole;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    public long getId() {
        return user.getId();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userRole.name()));
        return authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(user, that.user) &&
                userRole == that.userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, userRole);
    }

    public User getUser() {
        return user;
    }
}
