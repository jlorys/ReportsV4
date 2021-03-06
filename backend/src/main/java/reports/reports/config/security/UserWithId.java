package reports.reports.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserWithId extends User {
    private static final long serialVersionUID = 1L;
    private Integer id;

    UserWithId(String username, String password, Collection<? extends GrantedAuthority> authorities, boolean isEnabled, Integer id) {
        super(username, password, isEnabled, true, true, true, authorities);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}