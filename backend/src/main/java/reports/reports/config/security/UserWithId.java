package reports.reports.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Simple User that also keep track of the primary key.
 */
public class UserWithId extends User {
    private static final long serialVersionUID = 1L;
    private Integer id;

    public UserWithId(String username, String password, Collection<? extends GrantedAuthority> authorities, Integer id) {
        super(username, password, authorities);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}