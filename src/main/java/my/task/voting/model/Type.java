package my.task.voting.model;

import org.springframework.security.core.GrantedAuthority;

public enum Type implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
