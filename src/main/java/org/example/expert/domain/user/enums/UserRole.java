package org.example.expert.domain.user.enums;

import java.util.Collection;
import java.util.Collections;
import org.example.expert.domain.common.exception.InvalidRequestException;

import java.util.Arrays;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRole {
    ADMIN, USER;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("유효하지 않은 UerRole"));
    }

    public Collection<SimpleGrantedAuthority> getAuthorities() {
        switch (this) {
            case ADMIN:
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            case USER:
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            default:
                return Collections.emptyList();
        }
    }
}
