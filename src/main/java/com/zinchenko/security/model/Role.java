package com.zinchenko.security.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.zinchenko.security.model.Permission.*;

public enum Role {
    USER(Set.of(USER_ALL)),
    READONLY_ADMIN(Set.of(ADMIN_READ)),
    ADMIN(Set.of(ADMIN_READ, ADMIN_WRITE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet());
    }
}
