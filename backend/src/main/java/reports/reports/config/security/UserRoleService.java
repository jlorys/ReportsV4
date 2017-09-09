package reports.reports.config.security;

import org.springframework.stereotype.Service;

@Service
public class UserRoleService {

    public Boolean isLoggedUserHasRoleAdmin() { return UserContext.hasRole("Admin"); }

    public Boolean isLoggedUserHasRoleReviewer() {
        return UserContext.hasRole("Reviewer");
    }

    public Boolean isLoggedUserHasRoleUser() {
        return UserContext.hasRole("User");
    }
}
